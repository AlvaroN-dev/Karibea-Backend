package com.microservice.chatbot.domain.port.in;

import com.microservice.chatbot.application.dto.ConversationRequest;
import com.microservice.chatbot.domain.models.Conversation;

/**
 * Input port for creating new conversations.
 * Location: domain/port/in - Defines the use case contract.
 */
public interface CreateConversationUseCase {

    /**
     * Creates a new conversation.
     *
     * @param request the conversation creation request
     * @return the newly created conversation
     */
    Conversation createConversation(ConversationRequest request);
}
