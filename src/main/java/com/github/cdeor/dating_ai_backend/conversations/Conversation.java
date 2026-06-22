package com.github.cdeor.dating_ai_backend.conversations;

import java.util.List;

public record Conversation(
        String id,
        String profile_id,  // matched profile for this conversation
        String creator_id, //  profile that initiated conversation
        List<ChatMessage> messages  // Chats belonging to this conversation
) {
}
