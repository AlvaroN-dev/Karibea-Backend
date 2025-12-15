package com.microservice.chatbot.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a conversation is not found.
 * Location: domain/exceptions - No Spring dependencies.
 */
public class ConversationNotFoundException extends ChatDomainException {

    private final UUID conversationId;

    public ConversationNotFoundException(UUID conversationId) {
        super(String.format("Conversation not found with id: %s", conversationId));
        this.conversationId = conversationId;
    }

    public UUID getConversationId() {
        return conversationId;
    }
}
