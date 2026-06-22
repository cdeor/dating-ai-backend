package com.github.cdeor.dating_ai_backend.conversations;

import java.time.LocalDateTime;

public record ChatMessage(
        String messageText, // message text
        String authorId,    // person writing this messageText
        LocalDateTime messageTime
) {
}
