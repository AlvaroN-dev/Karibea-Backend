package com.microservice.catalog.domain.models;

import java.time.Instant;
import java.util.UUID;

/**
 * Image entity representing a product or variant image.
 */
public class Image {

    private final UUID id;
    private final UUID productId;
    private final UUID variantId; // Optional - null if product-level image
    private String url;
    private Integer displayOrder;
    private boolean primary;
    private Instant createdAt;

    /**
     * Private constructor to enforce creation through factory methods.
     */
    private Image(UUID id, UUID productId, UUID variantId, String url,
            Integer displayOrder, boolean primary, Instant createdAt) {
        this.id = id;
        this.productId = productId;
        this.variantId = variantId;
        this.url = url;
        this.displayOrder = displayOrder;
        this.primary = primary;
        this.createdAt = createdAt;
    }

    /**
     * Factory method to create a new Image for a product.
     */
    public static Image createForProduct(UUID productId, String url, Integer displayOrder, boolean primary) {
        validateRequired(productId, "productId");
        validateRequired(url, "url");

        return new Image(
                UUID.randomUUID(),
                productId,
                null,
                url.trim(),
                displayOrder != null ? displayOrder : 0,
                primary,
                Instant.now());
    }

    /**
     * Factory method to create a new Image for a variant.
     */
    public static Image createForVariant(UUID productId, UUID variantId, String url,
            Integer displayOrder, boolean primary) {
        validateRequired(productId, "productId");
        validateRequired(variantId, "variantId");
        validateRequired(url, "url");

        return new Image(
                UUID.randomUUID(),
                productId,
                variantId,
                url.trim(),
                displayOrder != null ? displayOrder : 0,
                primary,
                Instant.now());
    }

    /**
     * Reconstitutes an Image from persistence.
     */
    public static Image reconstitute(UUID id, UUID productId, UUID variantId, String url,
            Integer displayOrder, boolean primary, Instant createdAt) {
        return new Image(id, productId, variantId, url, displayOrder, primary, createdAt);
    }

    // ========== Business Operations ==========

    /**
     * Updates the image URL.
     */
    public void updateUrl(String url) {
        validateRequired(url, "url");
        this.url = url.trim();
    }

    /**
     * Updates the display order.
     */
    public void updateDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder != null ? displayOrder : 0;
    }

    /**
     * Sets this image as the primary image.
     */
    public void setAsPrimary() {
        this.primary = true;
    }

    /**
     * Unsets this image as the primary image.
     */
    public void unsetPrimary() {
        this.primary = false;
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

    public UUID getProductId() {
        return productId;
    }

    public UUID getVariantId() {
        return variantId;
    }

    public String getUrl() {
        return url;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public boolean isPrimary() {
        return primary;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Checks if this image belongs to a variant.
     */
    public boolean isVariantImage() {
        return variantId != null;
    }
}
