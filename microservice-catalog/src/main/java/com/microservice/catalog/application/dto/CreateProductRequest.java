package com.microservice.catalog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for creating a new product.
 */
@Schema(description = "Request payload for creating a new product")
public class CreateProductRequest {

    @Schema(description = "External store ID", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Store ID is required")
    private UUID storeId;

    @Schema(description = "Unique SKU for the product", example = "PROD-001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "SKU is required")
    @Size(min = 3, max = 100, message = "SKU must be between 3 and 100 characters")
    private String sku;

    @Schema(description = "Product name", example = "Premium Cotton T-Shirt", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @Schema(description = "Product description", example = "High-quality cotton t-shirt with modern design")
    private String description;

    @Schema(description = "Product brand", example = "Nike")
    @Size(max = 100, message = "Brand cannot exceed 100 characters")
    private String brand;

    @Schema(description = "Base price of the product", example = "29.99", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private BigDecimal basePrice;

    @Schema(description = "Compare at price (original price before discount)", example = "39.99")
    private BigDecimal compareAtPrice;

    @Schema(description = "Currency code (ISO 4217)", example = "USD", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
    private String currency;

    // Constructors
    public CreateProductRequest() {
    }

    public CreateProductRequest(UUID storeId, String sku, String name, String description,
            String brand, BigDecimal basePrice, BigDecimal compareAtPrice, String currency) {
        this.storeId = storeId;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.basePrice = basePrice;
        this.compareAtPrice = compareAtPrice;
        this.currency = currency;
    }

    // Getters and Setters
    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
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
}
