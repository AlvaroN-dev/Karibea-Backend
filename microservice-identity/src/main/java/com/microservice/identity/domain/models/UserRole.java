package com.microservice.identity.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserRole {
    private UUID id;
    private User user;
    private Role role;
    private LocalDateTime endedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


    // Getters
    public UUID getId() { return id; }

    public User getUser() { return user; }

    public Role getRole() { return role; }

    public LocalDateTime getEndedAt() { return endedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters protegidos
    protected void setId(UUID id) { this.id = id; }

    protected void setUser(User user) { this.user = user; }

    protected void setRole(Role role) { this.role = role; }
    protected void setEndedAt(LocalDateTime endedAt) { this.endedAt = endedAt; }

    protected void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    protected void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public void endRole() {
        this.endedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
