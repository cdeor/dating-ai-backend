package com.github.cdeor.dating_ai_backend.conversations;

import com.github.cdeor.dating_ai_backend.profiles.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class ConversationController {

    private final ProfileRepository profileRepository;
    private final ConversationRepository conversationRepository;


    public ConversationController(
            ProfileRepository profileRepository,
            ConversationRepository conversationRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.profileRepository = profileRepository;
    }

    @PostMapping("/conversations")
    Conversation createConversation(@RequestBody ConversationRequest request) {

        profileRepository.findById(request.profileId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND)
                );

        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                request.profileId,
                new ArrayList<>()
        );

        conversationRepository.save(conversation);
        return conversation;
    }

    @GetMapping("/conversations/{conversationId}")
    public Conversation getConversations(
            @PathVariable String conversationId
    ) {
        return conversationRepository
                .findById(conversationId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Conversation not found for id: " + conversationId)
                );
    }

    @GetMapping("/conversations")
    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    @PostMapping("/conversations/{conversationId}")
    public Conversation addMessageToConversation(
            @PathVariable String conversationId,
            @RequestBody ChatMessage chatMessage
    ) {

        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Conversation not found for id: " + conversationId)
                );

        profileRepository.findById(chatMessage.authorId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Profile not found for id: " + chatMessage.authorId())
                );

        ChatMessage msg = new ChatMessage(
                chatMessage.messageText(),
                chatMessage.authorId(),
                LocalDateTime.now()
        );

        conversation.messages().add(msg);

        conversationRepository.save(conversation);
        return conversation;
    }

    record ConversationRequest(
            String profileId
    ) {
    }

}
