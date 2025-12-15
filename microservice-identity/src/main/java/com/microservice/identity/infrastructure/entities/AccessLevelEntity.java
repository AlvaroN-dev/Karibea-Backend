package com.microservice.identity.infrastructure.entities;

import com.microservice.identity.domain.models.AccessLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for AccessLevel table.
 * Represents access levels/permissions in the database with automatic UUID and
 * timestamp generation.
 */
@Data
@Entity
@Table(name = "access_levels")
@AllArgsConstructor
@NoArgsConstructor
public class AccessLevelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * JPA lifecycle callback - executed before entity is persisted.
     * Automatically sets createdAt timestamp.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Domain Model Conversion Methods

    /**
     * Converts an AccessLevel domain model to an AccessLevelEntity.
     *
     * @param accessLevel the AccessLevel domain model
     * @return AccessLevelEntity
     */
    public static AccessLevelEntity fromDomainModel(AccessLevel accessLevel) {
        if (accessLevel == null) {
            return null;
        }

        AccessLevelEntity entity = new AccessLevelEntity();
        entity.setId(accessLevel.getId());
        entity.setName(accessLevel.getName());
        entity.setDescription(accessLevel.getDescription());
        entity.setCreatedAt(accessLevel.getCreatedAt());

        return entity;
    }

    /**
     * Converts this AccessLevelEntity to an AccessLevel domain model.
     * Uses reflection to set protected fields.
     *
     * @return AccessLevel domain model
     */
    public AccessLevel toDomainModel() {
        return new AccessLevel(
                this.id,
                this.name,
                this.description,
                this.createdAt);
    }

}
