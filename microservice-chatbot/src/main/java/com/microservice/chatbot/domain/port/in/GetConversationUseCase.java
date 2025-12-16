package com.microservice.chatbot.domain.port.in;

import com.microservice.chatbot.domain.models.Conversation;

import java.util.UUID;

/**
 * Input port for retrieving conversations.
 * Location: domain/port/in - Defines the use case contract.
 */
public interface GetConversationUseCase {

    /**
     * Retrieves a conversation by its ID.
     *
     * @param conversationId the UUID of the conversation
     * @return the conversation with its messages
     */
    Conversation getConversation(UUID conversationId);
}
