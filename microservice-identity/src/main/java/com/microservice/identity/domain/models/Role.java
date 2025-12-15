package com.microservice.identity.domain.models;

import java.util.UUID;

public class Role {
    private UUID id;
    private String name;
    private AccessLevel accessLevel;
    private String description;

    public Role(UUID id, String name, AccessLevel accessLevel, String description) {
        this.id = id;
        this.name = name;
        this.accessLevel = accessLevel;
        this.description = description;
    }

    public Role(String name, AccessLevel accessLevel, String description) {
        this.name = name;
        this.accessLevel = accessLevel;
        this.description = description;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public String getDescription() {
        return description;
    }

    // Setters protegidos
    protected void setId(UUID id) {
        this.id = id;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    // Domain Logic
    public void update(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }
        this.name = name;
        this.description = description;
    }
}
