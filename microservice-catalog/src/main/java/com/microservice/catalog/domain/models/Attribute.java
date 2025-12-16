package com.microservice.catalog.domain.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Attribute entity representing a product attribute type.
 * Examples: Size, Color, Material, etc.
 */
public class Attribute {

    private final UUID id;
    private String name;
    private String displayName;
    private String type;
    private Instant createdAt;
    private Instant updatedAt;

    private final List<AttributeValue> values;

    /**
     * Private constructor to enforce creation through factory methods.
     */
    private Attribute(UUID id, String name, String displayName, String type,
            Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.values = new ArrayList<>();
    }

    /**
     * Factory method to create a new Attribute.
     */
    public static Attribute create(String name, String displayName, String type) {
        validateRequired(name, "name");
        validateRequired(displayName, "displayName");

        return new Attribute(
                UUID.randomUUID(),
                name.trim().toUpperCase(),
                displayName.trim(),
                type,
                Instant.now(),
                Instant.now());
    }

    /**
     * Reconstitutes an Attribute from persistence.
     */
    public static Attribute reconstitute(UUID id, String name, String displayName, String type,
            Instant createdAt, Instant updatedAt) {
        return new Attribute(id, name, displayName, type, createdAt, updatedAt);
    }

    // ========== Business Operations ==========

    /**
     * Updates the attribute information.
     */
    public void update(String displayName, String type) {
        if (displayName != null && !displayName.isBlank()) {
            this.displayName = displayName.trim();
        }
        this.type = type;
        this.updatedAt = Instant.now();
    }

    /**
     * Adds a value to this attribute.
     */
    public void addValue(AttributeValue value) {
        validateRequired(value, "value");
        this.values.add(value);
        this.updatedAt = Instant.now();
    }

    /**
     * Removes a value from this attribute.
     */
    public void removeValue(UUID valueId) {
        validateRequired(valueId, "valueId");
        boolean removed = this.values.removeIf(v -> v.getId().equals(valueId));
        if (removed) {
            this.updatedAt = Instant.now();
        }
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

    public String getDisplayName() {
        return displayName;
    }

    public String getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Returns an unmodifiable view of values.
     */
    public List<AttributeValue> getValues() {
        return Collections.unmodifiableList(values);
    }

    /**
     * Adds values during reconstitution (used by repository).
     */
    public void addValueForReconstitution(AttributeValue value) {
        this.values.add(value);
    }
}
