package com.microservice.chatbot.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * JPA Entity for intents table.
 * Location: infrastructure/entities - JPA persistence entity.
 */
@Entity
@Table(name = "intents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "training_phrases", columnDefinition = "jsonb")
    private List<String> trainingPhrases;

    @Column(name = "action", length = 100)
    private String action;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
