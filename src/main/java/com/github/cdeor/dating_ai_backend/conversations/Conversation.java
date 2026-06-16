package com.github.cdeor.dating_ai_backend.conversations;

import java.util.List;

public record Conversation(
        String id,
        String profile_id,
        List<ChatMessage> messages
) {
}
