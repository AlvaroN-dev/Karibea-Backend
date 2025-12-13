package com.microservice.chatbot.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain event published when an escalation is created.
 * Location: domain/events - Domain event, no Spring dependencies.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EscalationCreatedEvent {

    private UUID escalationId;
    private UUID conversationId;
    private String reason;
    private String priority;
    private LocalDateTime timestamp;

    /**
     * Creates an escalation created event.
     */
    public static EscalationCreatedEvent of(UUID escalationId, UUID conversationId, String reason, String priority) {
        return EscalationCreatedEvent.builder()
                .escalationId(escalationId)
                .conversationId(conversationId)
                .reason(reason)
                .priority(priority)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
