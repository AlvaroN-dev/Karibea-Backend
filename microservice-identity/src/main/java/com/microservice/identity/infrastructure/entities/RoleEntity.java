package com.microservice.identity.infrastructure.entities;

import com.microservice.identity.domain.models.AccessLevel;
import com.microservice.identity.domain.models.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for Role table.
 * Represents user roles in the database with automatic UUID and timestamp
 * generation.
 */
@Data
@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "access_level_id", nullable = false)
    private AccessLevelEntity accessLevel;

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
     * Converts a Role domain model to a RoleEntity.
     *
     * @param role the Role domain model
     * @return RoleEntity
     */
    public static RoleEntity fromDomainModel(Role role) {
        if (role == null) {
            return null;
        }

        RoleEntity entity = new RoleEntity();
        entity.setId(role.getId());
        entity.setName(role.getName());
        entity.setDescription(role.getDescription());

        // Convert access level
        if (role.getAccessLevel() != null) {
            entity.setAccessLevel(AccessLevelEntity.fromDomainModel(role.getAccessLevel()));
        }

        return entity;
    }

    /**
     * Converts this RoleEntity to a Role domain model.
     *
     * @return Role domain model
     */
    public Role toDomainModel() {
        AccessLevel domainAccessLevel = this.accessLevel != null
                ? this.accessLevel.toDomainModel()
                : null;

        return new Role(
                this.id,
                this.name,
                domainAccessLevel,
                this.description);
    }
}
