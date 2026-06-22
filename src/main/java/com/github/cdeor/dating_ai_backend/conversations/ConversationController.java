package com.github.cdeor.dating_ai_backend.conversations;

import com.github.cdeor.dating_ai_backend.profiles.Profile;
import com.github.cdeor.dating_ai_backend.profiles.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
public class ConversationController {

    private final ProfileRepository profileRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationService conversationService;


    public ConversationController(
            ProfileRepository profileRepository,
            ConversationRepository conversationRepository,
            ConversationService conversationService
    ) {
        this.conversationRepository = conversationRepository;
        this.profileRepository = profileRepository;
        this.conversationService = conversationService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/conversations")
    Conversation createConversation(@RequestBody ConversationRequest request) {

        profileCheck(request.profileId, request.creatorId);

        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                request.profileId,
                request.creatorId,
                new ArrayList<>()
        );

        conversationRepository.save(conversation);
        return conversation;
    }

    @CrossOrigin(origins = "*")
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

    @CrossOrigin(origins = "*")
    @GetMapping("/conversations")
    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    @CrossOrigin(origins = "*")
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

        String matchId = conversation.profile_id();
        String userId = chatMessage.authorId();

        profileCheck(matchId, userId);

        Profile match = profileRepository.findById(matchId).get();
        Profile user = profileRepository.findById(userId).get();

        ChatMessage newChatMsg = new ChatMessage(
                chatMessage.messageText(),
                chatMessage.authorId(),
                LocalDateTime.now()
        );

        conversation.messages().add(newChatMsg);
        conversation = conversationService.generateAiResponse(conversation, match, user);
        conversationRepository.save(conversation);
        return conversation;
    }

    record ConversationRequest(
            String profileId,
            String creatorId
    ) {
    }

    private void profileCheck(String... profileIds) {

        Arrays.stream(profileIds)
                .filter(p -> !profileRepository.existsById(p))
                .findFirst()
                .ifPresent(profile -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Profile[" + profile + "] not found.");
                });
    }

    private void conversationCheck(String... conversationIds) {

        Arrays.stream(conversationIds)
                .filter(c -> !conversationRepository.existsById(c))
                .findFirst()
                .ifPresent(conversation -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Conversation[" + conversation + "] not found.");
                });
    }
}
