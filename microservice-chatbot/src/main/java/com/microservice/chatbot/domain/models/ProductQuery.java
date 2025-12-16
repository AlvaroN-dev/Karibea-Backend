package com.microservice.chatbot.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain model representing a product query made during a conversation.
 * Location: domain/models - Pure domain entity, no JPA annotations.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuery {

    private UUID id;
    private UUID messageId;
    private UUID externalProductId;
    private String queryText;
    private int resultsCount;
    private LocalDateTime createdAt;

    /**
     * Creates a new product query.
     */
    public static ProductQuery create(UUID messageId, String queryText) {
        return ProductQuery.builder()
                .id(UUID.randomUUID())
                .messageId(messageId)
                .queryText(queryText)
                .resultsCount(0)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Updates the results count after search execution.
     */
    public void updateResults(int count, UUID productId) {
        this.resultsCount = count;
        this.externalProductId = productId;
    }

    /**
     * Checks if the query returned results.
     */
    public boolean hasResults() {
        return this.resultsCount > 0;
    }
}
