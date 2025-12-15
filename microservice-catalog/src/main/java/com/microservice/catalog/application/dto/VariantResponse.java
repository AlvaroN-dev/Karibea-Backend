package com.microservice.catalog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for variant information.
 */
@Schema(description = "Variant information response")
public class VariantResponse {

    @Schema(description = "Unique identifier of the variant", example = "123e4567-e89b-12d3-a456-426614174002")
    private UUID id;

    @Schema(description = "Parent product ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID productId;

    @Schema(description = "Variant SKU", example = "PROD-001-M-RED")
    private String sku;

    @Schema(description = "Variant name", example = "Medium / Red")
    private String name;

    @Schema(description = "Variant price", example = "29.99")
    private BigDecimal price;

    @Schema(description = "Compare at price", example = "39.99")
    private BigDecimal compareAtPrice;

    @Schema(description = "Barcode", example = "1234567890123")
    private String barcode;

    @Schema(description = "Active status", example = "true")
    private boolean active;

    @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00Z")
    private Instant createdAt;

    @Schema(description = "Last update timestamp", example = "2024-01-15T12:45:00Z")
    private Instant updatedAt;

    @Schema(description = "Variant images")
    private List<ImageResponse> images;

    // Constructors
    public VariantResponse() {
    }

    public VariantResponse(UUID id, UUID productId, String sku, String name, BigDecimal price,
            BigDecimal compareAtPrice, String barcode, boolean active,
            Instant createdAt, Instant updatedAt, List<ImageResponse> images) {
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
        this.images = images;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCompareAtPrice() {
        return compareAtPrice;
    }

    public void setCompareAtPrice(BigDecimal compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public List<ImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ImageResponse> images) {
        this.images = images;
    }
}
