package com.microservice.chatbot.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain event published when a message is sent.
 * Location: domain/events - Domain event, no Spring dependencies.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSentEvent {

    private UUID messageId;
    private UUID conversationId;
    private String senderType;
    private String content;
    private LocalDateTime timestamp;

    /**
     * Creates a message sent event.
     */
    public static MessageSentEvent of(UUID messageId, UUID conversationId, String senderType, String content) {
        return MessageSentEvent.builder()
                .messageId(messageId)
                .conversationId(conversationId)
                .senderType(senderType)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
