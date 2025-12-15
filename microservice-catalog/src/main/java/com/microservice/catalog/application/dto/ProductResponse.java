package com.microservice.catalog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for product information.
 * Contains complete product data to be returned to clients.
 */
@Schema(description = "Product information response")
public class ProductResponse {

    @Schema(description = "Unique identifier of the product", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "External store ID", example = "123e4567-e89b-12d3-a456-426614174001")
    private UUID externalStoreId;

    @Schema(description = "Product SKU", example = "PROD-001")
    private String sku;

    @Schema(description = "Product name", example = "Premium Cotton T-Shirt")
    private String name;

    @Schema(description = "Product description", example = "High-quality cotton t-shirt")
    private String description;

    @Schema(description = "Product brand", example = "Nike")
    private String brand;

    @Schema(description = "Base price", example = "29.99")
    private BigDecimal basePrice;

    @Schema(description = "Compare at price", example = "39.99")
    private BigDecimal compareAtPrice;

    @Schema(description = "Currency code", example = "USD")
    private String currency;

    @Schema(description = "Product status", example = "PUBLISHED")
    private String status;

    @Schema(description = "Featured flag", example = "true")
    private boolean featured;

    @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00Z")
    private Instant createdAt;

    @Schema(description = "Last update timestamp", example = "2024-01-15T12:45:00Z")
    private Instant updatedAt;

    @Schema(description = "Product variants")
    private List<VariantResponse> variants;

    @Schema(description = "Product images")
    private List<ImageResponse> images;

    // Constructors
    public ProductResponse() {
    }

    public ProductResponse(UUID id, UUID externalStoreId, String sku, String name, String description,
            String brand, BigDecimal basePrice, BigDecimal compareAtPrice, String currency,
            String status, boolean featured, Instant createdAt, Instant updatedAt,
            List<VariantResponse> variants, List<ImageResponse> images) {
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
        this.variants = variants;
        this.images = images;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getExternalStoreId() {
        return externalStoreId;
    }

    public void setExternalStoreId(UUID externalStoreId) {
        this.externalStoreId = externalStoreId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getCompareAtPrice() {
        return compareAtPrice;
    }

    public void setCompareAtPrice(BigDecimal compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<VariantResponse> getVariants() {
        return variants;
    }

    public void setVariants(List<VariantResponse> variants) {
        this.variants = variants;
    }

    public List<ImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ImageResponse> images) {
        this.images = images;
    }
}
