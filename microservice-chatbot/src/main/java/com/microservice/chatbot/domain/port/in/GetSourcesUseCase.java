package com.microservice.chatbot.domain.port.in;

import com.microservice.chatbot.domain.models.ChatSource;

import java.util.List;

/**
 * Input port for retrieving available chat sources.
 * Location: domain/port/in - Defines the use case contract.
 */
public interface GetSourcesUseCase {

    /**
     * Retrieves all available sources for RAG context.
     *
     * @return list of available chat sources
     */
    List<ChatSource> getSources();
}
