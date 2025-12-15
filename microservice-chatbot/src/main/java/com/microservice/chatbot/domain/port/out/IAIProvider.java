package com.microservice.chatbot.domain.port.out;

import com.microservice.chatbot.application.dto.ChatApiResponse;
import com.microservice.chatbot.domain.models.ContextPayload;
import com.microservice.chatbot.domain.models.ModelMetadata;
import reactor.core.publisher.Flux;

/**
 * Output port for AI provider integration.
 * Provider-agnostic interface that can be implemented by AI21, OpenAI, etc.
 * Location: domain/port/out - External dependency contract.
 */
public interface IAIProvider {

    /**
     * Generates a response from the AI model.
     *
     * @param contextPayload the context containing system prompt, user message,
     *                       history, and sources
     * @return the chat API response with answer, sources, actions, and intent
     */
    ChatApiResponse generateResponse(ContextPayload contextPayload);

    /**
     * Streams a response from the AI model.
     *
     * @param contextPayload the context containing system prompt, user message,
     *                       history, and sources
     * @return a flux of response chunks
     */
    Flux<String> streamResponse(ContextPayload contextPayload);

    /**
     * Gets metadata about the AI model being used.
     *
     * @return the model metadata including provider, model name, and capabilities
     */
    ModelMetadata getModelMetadata();

    /**
     * Checks if the provider is properly configured and ready.
     *
     * @return true if the provider is configured and operational
     */
    boolean isConfigured();
}
