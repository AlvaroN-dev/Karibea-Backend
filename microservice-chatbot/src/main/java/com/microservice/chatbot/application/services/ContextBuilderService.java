package com.microservice.chatbot.application.services;

import com.microservice.chatbot.domain.models.ChatSource;
import com.microservice.chatbot.domain.models.ContextPayload;
import com.microservice.chatbot.domain.models.Message;
import com.microservice.chatbot.domain.port.out.RetrieverService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for building context for RAG processing.
 * Location: application/services - Orchestration service.
 */
@Service
@RequiredArgsConstructor
public class ContextBuilderService {

    private final RetrieverService retrieverService;
    private final SanitizationService sanitizationService;
    private final PromptTemplate promptTemplate;

    @Value("${rag.context.max-items:10}")
    private int maxContextItems;

    @Value("${rag.context.max-snippet-length:500}")
    private int maxSnippetLength;

    /**
     * Builds the complete context payload for AI generation.
     *
     * @param userMessage         the current user message
     * @param conversationHistory previous messages in the conversation
     * @return the context payload ready for AI processing
     */
    public ContextPayload buildContext(String userMessage, List<Message> conversationHistory) {
        // Sanitize user message
        String sanitizedMessage = sanitizationService.sanitize(userMessage);

        // Retrieve relevant context
        List<ChatSource> contextSources = retrieverService.retrieveContext(sanitizedMessage, maxContextItems);

        // Truncate sources to max length
        List<ChatSource> truncatedSources = contextSources.stream()
                .map(source -> source.truncate(maxSnippetLength))
                .collect(Collectors.toList());

        // Limit conversation history
        List<Message> limitedHistory = limitHistory(conversationHistory);

        // Build the formatted prompt
        String formattedPrompt = promptTemplate.buildPrompt(sanitizedMessage, limitedHistory, truncatedSources);

        return ContextPayload.builder()
                .systemPrompt(promptTemplate.getSystemPrompt())
                .userMessage(sanitizedMessage)
                .conversationHistory(limitedHistory)
                .contextSources(truncatedSources)
                .formattedPrompt(formattedPrompt)
                .build();
    }

    /**
     * Limits the conversation history to prevent token overflow.
     */
    private List<Message> limitHistory(List<Message> history) {
        if (history == null || history.isEmpty()) {
            return List.of();
        }

        // Keep last 10 messages maximum
        int maxHistory = 10;
        if (history.size() <= maxHistory) {
            return history;
        }

        return history.subList(history.size() - maxHistory, history.size());
    }
}
