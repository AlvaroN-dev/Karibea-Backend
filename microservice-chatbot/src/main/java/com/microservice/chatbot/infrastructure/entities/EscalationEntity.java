package com.microservice.chatbot.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for escalations table.
 * Location: infrastructure/entities - JPA persistence entity.
 */
@Entity
@Table(name = "escalations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EscalationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "conversation_id", nullable = false)
    private UUID conversationId;

    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "priority", length = 50)
    private String priority;

    @Column(name = "external_assigned_agent_id")
    private UUID externalAssignedAgentId;

    @Column(name = "statu_escalation_id")
    private UUID statuEscalationId;

    @Column(name = "escalated_at")
    private LocalDateTime escalatedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
}
