package com.microservice.chatbot.domain.port.in;

import com.microservice.chatbot.application.dto.ChatApiResponse;
import com.microservice.chatbot.application.dto.MessageRequest;

/**
 * Input port for sending messages to the chatbot.
 * Location: domain/port/in - Defines the use case contract.
 */
public interface SendMessageUseCase {

    /**
     * Processes a user message and generates an AI response.
     *
     * @param request the message request containing the user message and
     *                conversation context
     * @return the chat API response with answer, sources, actions, and intent
     */
    ChatApiResponse sendMessage(MessageRequest request);
}
