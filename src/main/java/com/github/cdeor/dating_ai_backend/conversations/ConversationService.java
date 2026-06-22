package com.github.cdeor.dating_ai_backend.conversations;

import com.github.cdeor.dating_ai_backend.profiles.Profile;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConversationService {

    private ChatClient chatClient;

    public ConversationService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public Conversation generateAiResponse(Conversation conversation,
                                           Profile profile,
                                           Profile user) {

        String systemMsgStr = SystemMessageBuilder.buildSystemMessage(profile, user);
        SystemMessage systemMessage = new SystemMessage(systemMsgStr);

//        sequence messages for openai

        List<? extends AbstractMessage> msgHistory = conversation.messages().stream()
                .map(msg ->
                        msg.authorId().equals(profile.id()) ?
                                new AssistantMessage(msg.messageText()) :
                                new UserMessage(msg.messageText()))
                .toList();

        List<Message> allMsgs = new ArrayList<>(conversation.messages().size());
        allMsgs.add(systemMessage);
        allMsgs.addAll(msgHistory);
        Prompt prompt = new Prompt(allMsgs);
        ChatResponse chatResponse = chatClient
                .prompt(prompt)
                .call().chatResponse();

        ChatMessage newMsg = new ChatMessage(
                chatResponse.getResult().getOutput().getText(),
                profile.id(),
                LocalDateTime.now()
        );

        conversation.messages().add(newMsg);
        return conversation;
    }
}
