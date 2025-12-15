package com.microservice.catalog.domain.models;

import com.microservice.catalog.domain.models.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Product is the Aggregate Root of the Catalog bounded context.
 * It encapsulates all product-related invariants and business rules.
 * 
 * Invariants protected:
 * - SKU must be unique within the store context
 * - Base price must be non-negative
 * - Published products must have at least one active variant
 * - Status transitions must follow valid state machine
 */
public class Product {

    private final UUID id;
    private final UUID externalStoreId;
    private String sku;
    private String name;
    private String description;
    private String brand;
    private BigDecimal basePrice;
    private BigDecimal compareAtPrice;
    private String currency;
    private ProductStatus status;
    private boolean featured;
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;

    private final List<Variant> variants;
    private final List<Image> images;
    private final List<ProductCategory> categories;

    /**
     * Private constructor to enforce creation through factory methods.
     */
    private Product(UUID id, UUID externalStoreId, String sku, String name, String description,
            String brand, BigDecimal basePrice, BigDecimal compareAtPrice, String currency,
            ProductStatus status, boolean featured, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.externalStoreId = externalStoreId;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.basePrice = basePrice;
        this.compareAtPrice = compareAtPrice;
        this.currency = currency;
        this.status = status;
        this.featured = featured;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.variants = new ArrayList<>();
        this.images = new ArrayList<>();
        this.categories = new ArrayList<>();
    }

    /**
     * Factory method to create a new Product.
     * Enforces required fields and initial invariants.
     */
    public static Product create(UUID externalStoreId, String sku, String name, String description,
            String brand, BigDecimal basePrice, BigDecimal compareAtPrice,
            String currency) {
        validateRequired(externalStoreId, "externalStoreId");
        validateRequired(sku, "sku");
        validateRequired(name, "name");
        validatePrice(basePrice, "basePrice");
        validateCurrency(currency);

        return new Product(
                UUID.randomUUID(),
                externalStoreId,
                sku.trim(),
                name.trim(),
                description,
                brand,
                basePrice,
                compareAtPrice,
                currency.toUpperCase(),
                ProductStatus.DRAFT,
                false,
                Instant.now(),
                Instant.now());
    }

    /**
     * Reconstitutes a Product from persistence.
     */
    public static Product reconstitute(UUID id, UUID externalStoreId, String sku, String name,
            String description, String brand, BigDecimal basePrice,
            BigDecimal compareAtPrice, String currency, ProductStatus status,
            boolean featured, Instant createdAt, Instant updatedAt, Long version) {
        Product product = new Product(id, externalStoreId, sku, name, description, brand,
                basePrice, compareAtPrice, currency, status, featured, createdAt, updatedAt);
        product.version = version;
        return product;
    }

    // ========== Business Operations ==========

    /**
     * Publishes the product, making it available for customers.
     * Enforces invariant: must have at least one active variant.
     */
    public void publish() {
        if (this.status == ProductStatus.INACTIVE) {
            throw new IllegalStateException("Cannot publish an inactive product. Reactivate it first.");
        }
        if (this.variants.isEmpty() || this.variants.stream().noneMatch(Variant::isActive)) {
            throw new IllegalStateException("Cannot publish product without at least one active variant.");
        }
        this.status = ProductStatus.PUBLISHED;
        this.updatedAt = Instant.now();
    }

    /**
     * Deactivates the product, removing it from the storefront.
     */
    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    /**
     * Sets the product as draft for editing.
     */
    public void setAsDraft() {
        if (this.status == ProductStatus.INACTIVE) {
            throw new IllegalStateException("Cannot set inactive product as draft. Reactivate it first.");
        }
        this.status = ProductStatus.DRAFT;
        this.updatedAt = Instant.now();
    }

    /**
     * Archives the product.
     */
    public void archive() {
        this.status = ProductStatus.ARCHIVED;
        this.updatedAt = Instant.now();
    }

    /**
     * Adds a variant to this product.
     * Enforces SKU uniqueness within the product.
     */
    public void addVariant(Variant variant) {
        validateRequired(variant, "variant");
        boolean skuExists = this.variants.stream()
                .anyMatch(v -> v.getSku().equalsIgnoreCase(variant.getSku()));
        if (skuExists) {
            throw new IllegalArgumentException("Variant with SKU '" + variant.getSku() + "' already exists.");
        }
        this.variants.add(variant);
        this.updatedAt = Instant.now();
    }

    /**
     * Removes a variant from this product.
     */
    public void removeVariant(UUID variantId) {
        validateRequired(variantId, "variantId");
        boolean removed = this.variants.removeIf(v -> v.getId().equals(variantId));
        if (removed) {
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Adds an image to this product.
     */
    public void addImage(Image image) {
        validateRequired(image, "image");
        this.images.add(image);
        this.updatedAt = Instant.now();
    }

    /**
     * Removes an image from this product.
     */
    public void removeImage(UUID imageId) {
        validateRequired(imageId, "imageId");
        boolean removed = this.images.removeIf(img -> img.getId().equals(imageId));
        if (removed) {
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Assigns a category to this product.
     */
    public void assignCategory(ProductCategory category) {
        validateRequired(category, "category");
        boolean exists = this.categories.stream()
                .anyMatch(c -> c.getCategoryId().equals(category.getCategoryId()));
        if (!exists) {
            this.categories.add(category);
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Removes a category from this product.
     */
    public void removeCategory(UUID categoryId) {
        validateRequired(categoryId, "categoryId");
        boolean removed = this.categories.removeIf(c -> c.getCategoryId().equals(categoryId));
        if (removed) {
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Updates the product basic information.
     */
    public void updateBasicInfo(String name, String description, String brand) {
        if (name != null && !name.isBlank()) {
            this.name = name.trim();
        }
        this.description = description;
        this.brand = brand;
        this.updatedAt = Instant.now();
    }

    /**
     * Updates the product pricing.
     */
    public void updatePricing(BigDecimal basePrice, BigDecimal compareAtPrice, String currency) {
        validatePrice(basePrice, "basePrice");
        if (currency != null) {
            validateCurrency(currency);
            this.currency = currency.toUpperCase();
        }
        this.basePrice = basePrice;
        this.compareAtPrice = compareAtPrice;
        this.updatedAt = Instant.now();
    }

    /**
     * Sets the featured flag.
     */
    public void setFeatured(boolean featured) {
        this.featured = featured;
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

    private static void validatePrice(BigDecimal price, String fieldName) {
        if (price == null) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative.");
        }
    }

    private static void validateCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency is required.");
        }
        if (currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be a 3-letter ISO code.");
        }
    }

    // ========== Getters ==========

    public UUID getId() {
        return id;
    }

    public UUID getExternalStoreId() {
        return externalStoreId;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public BigDecimal getCompareAtPrice() {
        return compareAtPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public boolean isFeatured() {
        return featured;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    /**
     * Returns an unmodifiable view of variants.
     */
    public List<Variant> getVariants() {
        return Collections.unmodifiableList(variants);
    }

    /**
     * Returns an unmodifiable view of images.
     */
    public List<Image> getImages() {
        return Collections.unmodifiableList(images);
    }

    /**
     * Returns an unmodifiable view of categories.
     */
    public List<ProductCategory> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    /**
     * Adds variants during reconstitution (used by repository).
     */
    public void addVariantForReconstitution(Variant variant) {
        this.variants.add(variant);
    }

    /**
     * Adds images during reconstitution (used by repository).
     */
    public void addImageForReconstitution(Image image) {
        this.images.add(image);
    }

    /**
     * Adds categories during reconstitution (used by repository).
     */
    public void addCategoryForReconstitution(ProductCategory category) {
        this.categories.add(category);
    }
}
