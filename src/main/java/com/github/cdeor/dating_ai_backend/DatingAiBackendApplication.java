package com.github.cdeor.dating_ai_backend;

import com.github.cdeor.dating_ai_backend.conversations.ChatMessage;
import com.github.cdeor.dating_ai_backend.conversations.Conversation;
import com.github.cdeor.dating_ai_backend.conversations.ConversationRepository;
import com.github.cdeor.dating_ai_backend.profiles.Gender;
import com.github.cdeor.dating_ai_backend.profiles.Profile;
import com.github.cdeor.dating_ai_backend.profiles.ProfileRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class DatingAiBackendApplication implements CommandLineRunner {


    private ChatClient chatClient;
    private ProfileRepository profileRepository;
    private ConversationRepository conversationRepository;

    public DatingAiBackendApplication(ProfileRepository profileRepository,
                                      ConversationRepository conversationRepository,
                                      @Qualifier("openAiChatModel") ChatModel chatModel
    ) {
        this.profileRepository = profileRepository;
        this.conversationRepository = conversationRepository;
        this.chatClient = ChatClient.create(chatModel);
    }

    public static void main(String[] args) {
        SpringApplication.run(DatingAiBackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("App running");
        profileRepository.deleteAll();
        conversationRepository.deleteAll();

        Profile profile1 = new Profile(
                "1",
                "shubh",
                "pandey",
                32,
                "Indian",
                Gender.MALE,
                "Software Engineer",
                "selfie.png",
                "INTROVERT"
        );

        Profile profile2 = new Profile(
                "2",
                "shubh",
                "pandey",
                32,
                "Indian",
                Gender.MALE,
                "Software Engineer",
                "selfie.png",
                "INTROVERT"
        );

        profileRepository.save(profile1);
        profileRepository.save(profile2);
        profileRepository
                .findAll()
                .forEach(System.out::println);

        Conversation conversation = new Conversation(
                "1",
                profile1.id(),
                List.of(new ChatMessage(
                        "1",
                        "2",
                        LocalDateTime.now()
                ))
        );

        conversationRepository.save(conversation);
        conversationRepository.findAll()
                .forEach(System.out::println);

        String res = chatClient.prompt()
                .user("Testing")
                .call()
                .content();

        System.out.println(res);

    }
}
