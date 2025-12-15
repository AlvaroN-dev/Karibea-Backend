package com.microservice.chatbot.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Value object representing a source of information for RAG.
 * Location: domain/models - Value object for source references.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSource {

    private String id;
    private SourceType type;
    private String content;
    private BigDecimal score;

    /**
     * Enum representing the type of source.
     */
    public enum SourceType {
        PRODUCT,
        ORDER,
        FAQ,
        POLICY,
        CATEGORY,
        USER_PROFILE,
        CUSTOM
    }

    /**
     * Creates a product source.
     */
    public static ChatSource productSource(String id, String content, BigDecimal score) {
        return ChatSource.builder()
                .id(id)
                .type(SourceType.PRODUCT)
                .content(content)
                .score(score)
                .build();
    }

    /**
     * Creates an FAQ source.
     */
    public static ChatSource faqSource(String id, String content, BigDecimal score) {
        return ChatSource.builder()
                .id(id)
                .type(SourceType.FAQ)
                .content(content)
                .score(score)
                .build();
    }

    /**
     * Truncates the content to a maximum length.
     */
    public ChatSource truncate(int maxLength) {
        if (content == null || content.length() <= maxLength) {
            return this;
        }
        return ChatSource.builder()
                .id(this.id)
                .type(this.type)
                .content(content.substring(0, maxLength) + "...")
                .score(this.score)
                .build();
    }
}
