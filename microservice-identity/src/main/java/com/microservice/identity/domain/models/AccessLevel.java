package com.microservice.identity.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccessLevel {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;

    public AccessLevel(String name, String description) {
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    // el que usara  entity
    public AccessLevel(UUID id, String name, String description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }


    // Getters
    public UUID getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters protegidos
    protected void setId(UUID id) { this.id = id; }

    protected void setName(String name) { this.name = name; }

    protected void setDescription(String description) { this.description = description; }

    protected void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
