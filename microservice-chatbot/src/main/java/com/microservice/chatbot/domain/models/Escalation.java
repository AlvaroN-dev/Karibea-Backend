package com.microservice.chatbot.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain model representing an escalation to a human agent.
 * Location: domain/models - Pure domain entity, no JPA annotations.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Escalation {

    private UUID id;
    private UUID conversationId;
    private String reason;
    private Priority priority;
    private UUID externalAssignedAgentId;
    private UUID statusId;
    private LocalDateTime escalatedAt;
    private LocalDateTime resolvedAt;

    /**
     * Enum representing escalation priority levels.
     */
    public enum Priority {
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }

    /**
     * Creates a new escalation request.
     */
    public static Escalation createEscalation(UUID conversationId, String reason, Priority priority) {
        return Escalation.builder()
                .id(UUID.randomUUID())
                .conversationId(conversationId)
                .reason(reason)
                .priority(priority)
                .escalatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Assigns an agent to handle the escalation.
     */
    public void assignAgent(UUID agentId) {
        if (this.resolvedAt != null) {
            throw new IllegalStateException("Cannot assign agent to resolved escalation");
        }
        this.externalAssignedAgentId = agentId;
    }

    /**
     * Resolves the escalation.
     */
    public void resolve() {
        if (this.resolvedAt != null) {
            throw new IllegalStateException("Escalation is already resolved");
        }
        this.resolvedAt = LocalDateTime.now();
    }

    /**
     * Checks if the escalation is pending.
     */
    public boolean isPending() {
        return this.resolvedAt == null;
    }

    /**
     * Checks if the escalation is assigned.
     */
    public boolean isAssigned() {
        return this.externalAssignedAgentId != null;
    }
}
