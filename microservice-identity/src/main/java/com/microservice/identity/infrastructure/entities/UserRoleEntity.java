package com.microservice.identity.infrastructure.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * JPA Entity for UserRole join table.
 * Represents the many-to-many relationship between users and roles with
 * automatic timestamp generation.
 */
@Entity
@Table(name = "user_roles")
public class UserRoleEntity implements Serializable {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private transient UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private transient RoleEntity role;

    @Column(name = "assigned_at", nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    /**
     * JPA lifecycle callback - executed before entity is persisted.
     * Automatically sets assignedAt timestamp.
     */
    @PrePersist
    protected void onCreate() {
        this.assignedAt = LocalDateTime.now();
    }

    // Constructors
    public UserRoleEntity() {
    }

    public UserRoleEntity(UserEntity user, RoleEntity role) {
        this.user = user;
        this.role = role;
        this.id = new UserRoleId(user.getId(), role.getId());
    }

    // Getters and Setters
    public UserRoleId getId() {
        return id;
    }

    public void setId(UserRoleId id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    /**
     * Composite primary key for UserRole entity.
     */
    @Embeddable
    public static class UserRoleId implements Serializable {

        @Column(name = "user_id")
        private UUID userId;

        @Column(name = "role_id")
        private UUID roleId;

        public UserRoleId() {
        }

        public UserRoleId(UUID userId, UUID roleId) {
            this.userId = userId;
            this.roleId = roleId;
        }

        // Getters and Setters
        public UUID getUserId() {
            return userId;
        }

        public void setUserId(UUID userId) {
            this.userId = userId;
        }

        public UUID getRoleId() {
            return roleId;
        }

        public void setRoleId(UUID roleId) {
            this.roleId = roleId;
        }

        // equals and hashCode
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            UserRoleId that = (UserRoleId) o;
            return Objects.equals(userId, that.userId) && Objects.equals(roleId, that.roleId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, roleId);
        }
    }
}
