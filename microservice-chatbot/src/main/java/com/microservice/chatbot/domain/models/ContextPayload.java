package com.microservice.chatbot.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Value object representing the context payload for AI generation.
 * Contains all information needed to generate a response.
 * Location: domain/models - Value object for RAG context.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContextPayload {

    private String systemPrompt;
    private String userMessage;

    @Builder.Default
    private List<Message> conversationHistory = new ArrayList<>();

    @Builder.Default
    private List<ChatSource> contextSources = new ArrayList<>();

    private String formattedPrompt;

    /**
     * Builder method to add a source to the context.
     */
    public ContextPayload addSource(ChatSource source) {
        if (this.contextSources == null) {
            this.contextSources = new ArrayList<>();
        }
        this.contextSources.add(source);
        return this;
    }

    /**
     * Checks if the context has any sources.
     */
    public boolean hasSources() {
        return contextSources != null && !contextSources.isEmpty();
    }

    /**
     * Gets the total number of tokens (approximate) for the context.
     * Rough estimation: 1 token ≈ 4 characters.
     */
    public int estimateTokenCount() {
        int total = 0;

        if (systemPrompt != null) {
            total += systemPrompt.length() / 4;
        }
        if (userMessage != null) {
            total += userMessage.length() / 4;
        }
        for (Message msg : conversationHistory) {
            if (msg.getContent() != null) {
                total += msg.getContent().length() / 4;
            }
        }
        for (ChatSource source : contextSources) {
            if (source.getContent() != null) {
                total += source.getContent().length() / 4;
            }
        }

        return total;
    }
}
