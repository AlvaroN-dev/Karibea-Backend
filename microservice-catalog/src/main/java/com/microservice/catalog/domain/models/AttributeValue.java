package com.microservice.catalog.domain.models;

import java.time.Instant;
import java.util.UUID;

/**
 * AttributeValue entity representing a specific value for an attribute.
 * Examples: "Red" for Color, "M" for Size, etc.
 */
public class AttributeValue {

    private final UUID id;
    private final UUID attributeId;
    private String value;
    private String displayValue;
    private String colorHex;
    private Integer displayOrder;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Private constructor to enforce creation through factory methods.
     */
    private AttributeValue(UUID id, UUID attributeId, String value, String displayValue,
            String colorHex, Integer displayOrder, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.attributeId = attributeId;
        this.value = value;
        this.displayValue = displayValue;
        this.colorHex = colorHex;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Factory method to create a new AttributeValue.
     */
    public static AttributeValue create(UUID attributeId, String value, String displayValue,
            String colorHex, Integer displayOrder) {
        validateRequired(attributeId, "attributeId");
        validateRequired(value, "value");

        return new AttributeValue(
                UUID.randomUUID(),
                attributeId,
                value.trim(),
                displayValue != null ? displayValue.trim() : value.trim(),
                colorHex,
                displayOrder != null ? displayOrder : 0,
                Instant.now(),
                Instant.now());
    }

    /**
     * Reconstitutes an AttributeValue from persistence.
     */
    public static AttributeValue reconstitute(UUID id, UUID attributeId, String value,
            String displayValue, String colorHex, Integer displayOrder,
            Instant createdAt, Instant updatedAt) {
        return new AttributeValue(id, attributeId, value, displayValue, colorHex,
                displayOrder, createdAt, updatedAt);
    }

    // ========== Business Operations ==========

    /**
     * Updates the attribute value information.
     */
    public void update(String displayValue, String colorHex, Integer displayOrder) {
        if (displayValue != null && !displayValue.isBlank()) {
            this.displayValue = displayValue.trim();
        }
        this.colorHex = colorHex;
        this.displayOrder = displayOrder != null ? displayOrder : this.displayOrder;
        this.updatedAt = Instant.now();
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

    public UUID getAttributeId() {
        return attributeId;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public String getColorHex() {
        return colorHex;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Checks if this is a color attribute value.
     */
    public boolean hasColorHex() {
        return colorHex != null && !colorHex.isBlank();
    }
}
