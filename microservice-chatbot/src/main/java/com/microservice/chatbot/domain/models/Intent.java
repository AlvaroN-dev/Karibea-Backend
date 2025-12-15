package com.microservice.chatbot.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Domain model representing an intent detected from user messages.
 * Location: domain/models - Pure domain entity, no JPA annotations.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Intent {

    private UUID id;
    private String name;
    private String description;
    private List<String> trainingPhrases;
    private String action;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Common intent names for the chatbot.
     */
    public static class IntentNames {
        public static final String PRODUCT_SEARCH = "product_search";
        public static final String ORDER_STATUS = "order_status";
        public static final String PRICE_INQUIRY = "price_inquiry";
        public static final String SHIPPING_INFO = "shipping_info";
        public static final String RETURN_REQUEST = "return_request";
        public static final String COMPLAINT = "complaint";
        public static final String GENERAL_QUESTION = "general_question";
        public static final String GREETING = "greeting";
        public static final String FAREWELL = "farewell";
        public static final String ESCALATE = "escalate";
        public static final String UNKNOWN = "unknown";

        private IntentNames() {
        }
    }

    /**
     * Checks if this intent matches any of the training phrases.
     */
    public boolean matchesPhrase(String phrase) {
        if (phrase == null || trainingPhrases == null) {
            return false;
        }
        String lowerPhrase = phrase.toLowerCase();
        return trainingPhrases.stream()
                .anyMatch(tp -> lowerPhrase.contains(tp.toLowerCase()));
    }

    /**
     * Activates the intent.
     */
    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Deactivates the intent.
     */
    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }
}
