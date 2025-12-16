package com.microservice.catalog.domain.models;

import java.util.UUID;

/**
 * VariantAttributeValue is a Value Object representing the association
 * between a Variant and an AttributeValue.
 * 
 * This is immutable and represents which attribute values apply to a variant.
 */
public class VariantAttributeValue {

    private final UUID id;
    private final UUID variantId;
    private final UUID valueId;

    /**
     * Private constructor to enforce creation through factory methods.
     */
    private VariantAttributeValue(UUID id, UUID variantId, UUID valueId) {
        this.id = id;
        this.variantId = variantId;
        this.valueId = valueId;
    }

    /**
     * Factory method to create a new VariantAttributeValue.
     */
    public static VariantAttributeValue create(UUID variantId, UUID valueId) {
        validateRequired(variantId, "variantId");
        validateRequired(valueId, "valueId");

        return new VariantAttributeValue(UUID.randomUUID(), variantId, valueId);
    }

    /**
     * Reconstitutes a VariantAttributeValue from persistence.
     */
    public static VariantAttributeValue reconstitute(UUID id, UUID variantId, UUID valueId) {
        return new VariantAttributeValue(id, variantId, valueId);
    }

    // ========== Validation Helpers ==========

    private static void validateRequired(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " is required and cannot be null.");
        }
    }

    // ========== Getters ==========

    public UUID getId() {
        return id;
    }

    public UUID getVariantId() {
        return variantId;
    }

    public UUID getValueId() {
        return valueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        VariantAttributeValue that = (VariantAttributeValue) o;
        return variantId.equals(that.variantId) && valueId.equals(that.valueId);
    }

    @Override
    public int hashCode() {
        return 31 * variantId.hashCode() + valueId.hashCode();
    }
}
