package com.microservice.catalog.domain.models;

import java.time.Instant;
import java.util.UUID;

/**
 * Status entity representing the status lookup table.
 * This maps to the status table in the database.
 */
public class Status {

    private final UUID id;
    private final String name;
    private final String description;
    private final Instant createdAt;

    /**
     * Private constructor to enforce creation through factory methods.
     */
    private Status(UUID id, String name, String description, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    /**
     * Factory method to create a new Status.
     */
    public static Status create(String name, String description) {
        validateRequired(name, "name");

        return new Status(
                UUID.randomUUID(),
                name.trim().toUpperCase(),
                description,
                Instant.now());
    }

    /**
     * Reconstitutes a Status from persistence.
     */
    public static Status reconstitute(UUID id, String name, String description, Instant createdAt) {
        return new Status(id, name, description, createdAt);
    }

    // ========== Validation Helpers ==========

    private static void validateRequired(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " is required and cannot be null.");
        }
        if (value instanceof String && ((String) value).isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required and cannot be blank.");
        }
    }

    // ========== Getters ==========

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Status status = (Status) o;
        return id.equals(status.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
