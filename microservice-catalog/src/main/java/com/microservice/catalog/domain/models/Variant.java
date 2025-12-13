package com.microservice.catalog.domain.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Variant entity representing a specific purchasable version of a Product.
 * Examples: Size M, Color Red, etc.
 * 
 * This is an entity within the Product aggregate.
 */
public class Variant {

    private final UUID id;
    private final UUID productId;
    private String sku;
    private String name;
    private BigDecimal price;
    private BigDecimal compareAtPrice;
    private String barcode;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    private final List<VariantAttributeValue> attributeValues;
    private final List<Image> images;

    /**
     * Private constructor to enforce creation through factory methods.
     */
    private Variant(UUID id, UUID productId, String sku, String name, BigDecimal price,
            BigDecimal compareAtPrice, String barcode, boolean active,
            Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.productId = productId;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.compareAtPrice = compareAtPrice;
        this.barcode = barcode;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.attributeValues = new ArrayList<>();
        this.images = new ArrayList<>();
    }

    /**
     * Factory method to create a new Variant.
     */
    public static Variant create(UUID productId, String sku, String name, BigDecimal price,
            BigDecimal compareAtPrice, String barcode) {
        validateRequired(productId, "productId");
        validateRequired(sku, "sku");
        validateRequired(name, "name");
        validatePrice(price);

        return new Variant(
                UUID.randomUUID(),
                productId,
                sku.trim(),
                name.trim(),
                price,
                compareAtPrice,
                barcode,
                true,
                Instant.now(),
                Instant.now());
    }

    /**
     * Reconstitutes a Variant from persistence.
     */
    public static Variant reconstitute(UUID id, UUID productId, String sku, String name,
            BigDecimal price, BigDecimal compareAtPrice, String barcode,
            boolean active, Instant createdAt, Instant updatedAt) {
        return new Variant(id, productId, sku, name, price, compareAtPrice, barcode,
                active, createdAt, updatedAt);
    }

    // ========== Business Operations ==========

    /**
     * Updates variant information.
     */
    public void update(String name, BigDecimal price, BigDecimal compareAtPrice, String barcode) {
        if (name != null && !name.isBlank()) {
            this.name = name.trim();
        }
        if (price != null) {
            validatePrice(price);
            this.price = price;
        }
        this.compareAtPrice = compareAtPrice;
        this.barcode = barcode;
        this.updatedAt = Instant.now();
    }

    /**
     * Activates this variant.
     */
    public void activate() {
        this.active = true;
        this.updatedAt = Instant.now();
    }

    /**
     * Deactivates this variant.
     */
    public void deactivate() {
        this.active = false;
        this.updatedAt = Instant.now();
    }

    /**
     * Adds an attribute value to this variant.
     */
    public void addAttributeValue(VariantAttributeValue attributeValue) {
        validateRequired(attributeValue, "attributeValue");
        this.attributeValues.add(attributeValue);
        this.updatedAt = Instant.now();
    }

    /**
     * Removes an attribute value from this variant.
     */
    public void removeAttributeValue(UUID valueId) {
        validateRequired(valueId, "valueId");
        this.attributeValues.removeIf(av -> av.getValueId().equals(valueId));
        this.updatedAt = Instant.now();
    }

    /**
     * Adds an image to this variant.
     */
    public void addImage(Image image) {
        validateRequired(image, "image");
        this.images.add(image);
        this.updatedAt = Instant.now();
    }

    /**
     * Removes an image from this variant.
     */
    public void removeImage(UUID imageId) {
        validateRequired(imageId, "imageId");
        this.images.removeIf(img -> img.getId().equals(imageId));
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

    private static void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("Price is required.");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
    }

    // ========== Getters ==========

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getCompareAtPrice() {
        return compareAtPrice;
    }

    public String getBarcode() {
        return barcode;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Returns an unmodifiable view of attribute values.
     */
    public List<VariantAttributeValue> getAttributeValues() {
        return Collections.unmodifiableList(attributeValues);
    }

    /**
     * Returns an unmodifiable view of images.
     */
    public List<Image> getImages() {
        return Collections.unmodifiableList(images);
    }

    /**
     * Adds attribute values during reconstitution (used by repository).
     */
    public void addAttributeValueForReconstitution(VariantAttributeValue attributeValue) {
        this.attributeValues.add(attributeValue);
    }

    /**
     * Adds images during reconstitution (used by repository).
     */
    public void addImageForReconstitution(Image image) {
        this.images.add(image);
    }
}
