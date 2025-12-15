package com.microservice.chatbot.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Domain model representing a chat message.
 * Location: domain/models - Pure domain entity, no JPA annotations.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private UUID id;
    private UUID conversationId;
    private SenderType senderType;
    private UUID externalSenderId;
    private UUID messageTypeId;
    private String content;
    private Map<String, Object> metadata;
    private String intent;
    private BigDecimal confidence;
    private boolean read;
    private LocalDateTime createdAt;

    /**
     * Enum representing the type of sender.
     */
    public enum SenderType {
        USER,
        ASSISTANT,
        SYSTEM
    }

    /**
     * Creates a user message.
     */
    public static Message createUserMessage(UUID conversationId, UUID externalSenderId, String content) {
        return Message.builder()
                .id(UUID.randomUUID())
                .conversationId(conversationId)
                .senderType(SenderType.USER)
                .externalSenderId(externalSenderId)
                .content(content)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Creates an assistant message.
     */
    public static Message createAssistantMessage(UUID conversationId, String content, String intent,
            BigDecimal confidence) {
        return Message.builder()
                .id(UUID.randomUUID())
                .conversationId(conversationId)
                .senderType(SenderType.ASSISTANT)
                .content(content)
                .intent(intent)
                .confidence(confidence)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a system message.
     */
    public static Message createSystemMessage(UUID conversationId, String content) {
        return Message.builder()
                .id(UUID.randomUUID())
                .conversationId(conversationId)
                .senderType(SenderType.SYSTEM)
                .content(content)
                .read(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Marks the message as read.
     */
    public void markAsRead() {
        this.read = true;
    }

    /**
     * Validates that the message content is not empty.
     */
    public boolean isValid() {
        return content != null && !content.trim().isEmpty();
    }
}
