package com.microservice.identity.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserEventsProvider {
    private UUID id;
    private User user;
    private String provider;
    private String providerUserId;
    private String email;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserEventsProvider(User user, String provider, String providerUserId, String email, String name) {
        this.user = user;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.email = email;
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() { return id; }

    public User getUser() { return user; }

    public String getProvider() { return provider; }

    public String getProviderUserId() { return providerUserId; }

    public String getEmail() { return email; }

    public String getName() { return name; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }


    // Setters protegidos
    protected void setId(UUID id) { this.id = id; }

    protected void setUser(User user) { this.user = user; }

    protected void setProvider(String provider) { this.provider = provider; }

    protected void setProviderUserId(String providerUserId) { this.providerUserId = providerUserId; }

    protected void setEmail(String email) { this.email = email; }

    protected void setName(String name) { this.name = name; }

    protected void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    protected void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}


