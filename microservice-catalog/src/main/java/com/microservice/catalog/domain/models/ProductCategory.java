package com.microservice.catalog.domain.models;

import java.util.UUID;

/**
 * ProductCategory is a Value Object representing the association
 * between a Product and an external Category.
 * 
 * Note: Categories are managed by another microservice (microservice-store).
 * This only holds the reference and primary flag.
 */
public class ProductCategory {

    private final UUID id;
    private final UUID productId;
    private final UUID categoryId; // External reference to category service
    private boolean primary;

    /**
     * Private constructor to enforce creation through factory methods.
     */
    private ProductCategory(UUID id, UUID productId, UUID categoryId, boolean primary) {
        this.id = id;
        this.productId = productId;
        this.categoryId = categoryId;
        this.primary = primary;
    }

    /**
     * Factory method to create a new ProductCategory.
     */
    public static ProductCategory create(UUID productId, UUID categoryId, boolean primary) {
        validateRequired(productId, "productId");
        validateRequired(categoryId, "categoryId");

        return new ProductCategory(UUID.randomUUID(), productId, categoryId, primary);
    }

    /**
     * Reconstitutes a ProductCategory from persistence.
     */
    public static ProductCategory reconstitute(UUID id, UUID productId, UUID categoryId, boolean primary) {
        return new ProductCategory(id, productId, categoryId, primary);
    }

    // ========== Business Operations ==========

    /**
     * Sets this category as the primary category.
     */
    public void setAsPrimary() {
        this.primary = true;
    }

    /**
     * Unsets this category as the primary category.
     */
    public void unsetPrimary() {
        this.primary = false;
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

    public UUID getProductId() {
        return productId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public boolean isPrimary() {
        return primary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductCategory that = (ProductCategory) o;
        return productId.equals(that.productId) && categoryId.equals(that.categoryId);
    }

    @Override
    public int hashCode() {
        return 31 * productId.hashCode() + categoryId.hashCode();
    }
}
