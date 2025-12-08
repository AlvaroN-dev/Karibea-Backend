package com.microservice.identity.infrastructure.entities;

import com.microservice.identity.domain.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA Entity for User table.
 * Represents user accounts in the database with automatic UUID and timestamp
 * generation.
 */
@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "verification_token", length = 255)
    private String verificationToken;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    /**
     * JPA lifecycle callback - executed before entity is persisted.
     * Automatically sets createdAt and updatedAt timestamps.
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * JPA lifecycle callback - executed before entity is updated.
     * Automatically updates the updatedAt timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Domain Model Conversion Methods

    /**
     * Converts a User domain model to a UserEntity.
     * Uses reflection to access protected setters in the domain model.
     *
     * @param user the User domain model
     * @return UserEntity
     */
    public static UserEntity fromDomainModel(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setEnabled(user.isEnabled());
        entity.setEmailVerified(user.isEmailVerified());
        entity.setVerified(user.isVerified());
        entity.setLastLogin(user.getLastLogin());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        entity.setDeletedAt(user.getDeletedAt());
        entity.setDeleted(user.isDeleted());
        entity.setVerificationToken(user.getVerificationToken());

        // Convert roles
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            Set<RoleEntity> roleEntities = user.getRoles().stream()
                    .map(RoleEntity::fromDomainModel)
                    .collect(Collectors.toSet());
            entity.setRoles(roleEntities);
        }

        return entity;
    }

    /**
     * Converts this UserEntity to a User domain model.
     * Uses reflection to set protected fields in the domain model.
     *
     * @return User domain model
     */
    /**
     * Converts this UserEntity to a User domain model.
     * Uses the static reconstruct method on the Domain Model to bypass
     * encapsulation safely.
     *
     * @return User domain model
     */
    public User toDomainModel() {
        Set<com.microservice.identity.domain.models.Role> domainRoles = null;
        if (this.roles != null && !this.roles.isEmpty()) {
            domainRoles = this.roles.stream()
                    .map(RoleEntity::toDomainModel)
                    .collect(Collectors.toSet());
        }

        return User.builder()
                .id(this.id)
                .username(this.username)
                .email(this.email)
                .passwordHash(this.passwordHash)
                .enabled(this.enabled)
                .emailVerified(this.emailVerified)
                .isVerified(this.isVerified)
                .lastLogin(this.lastLogin)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .deletedAt(this.deletedAt)
                .isDeleted(this.isDeleted)
                .verificationToken(this.verificationToken)
                .roles(domainRoles)
                .build();
    }
}
