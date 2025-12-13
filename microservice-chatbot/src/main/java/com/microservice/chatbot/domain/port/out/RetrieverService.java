package com.microservice.chatbot.domain.port.out;

import com.microservice.chatbot.domain.models.ChatSource;

import java.util.List;

/**
 * Output port for retrieving context for RAG.
 * Location: domain/port/out - External dependency contract.
 */
public interface RetrieverService {

    /**
     * Retrieves relevant context sources based on a query.
     *
     * @param query      the search query (usually the user's message)
     * @param maxResults maximum number of results to return
     * @return list of relevant chat sources with scores
     */
    List<ChatSource> retrieveContext(String query, int maxResults);

    /**
     * Retrieves all available sources.
     *
     * @return list of all available chat sources
     */
    List<ChatSource> getAllSources();

    /**
     * Retrieves context sources by type.
     *
     * @param type       the source type to filter by
     * @param maxResults maximum number of results to return
     * @return list of chat sources of the specified type
     */
    List<ChatSource> retrieveByType(ChatSource.SourceType type, int maxResults);
}
