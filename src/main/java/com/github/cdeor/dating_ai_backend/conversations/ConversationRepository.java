package com.github.cdeor.dating_ai_backend.conversations;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConversationRepository extends  MongoRepository<Conversation, String> {
}
