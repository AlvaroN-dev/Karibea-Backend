package com.microservice.catalog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Request DTO for updating an existing product.
 */
@Schema(description = "Request payload for updating an existing product")
public class UpdateProductRequest {

    @Schema(description = "Product name", example = "Premium Cotton T-Shirt V2")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @Schema(description = "Product description", example = "Updated high-quality cotton t-shirt")
    private String description;

    @Schema(description = "Product brand", example = "Adidas")
    @Size(max = 100, message = "Brand cannot exceed 100 characters")
    private String brand;

    @Schema(description = "Base price of the product", example = "34.99")
    @Positive(message = "Base price must be positive")
    private BigDecimal basePrice;

    @Schema(description = "Compare at price", example = "44.99")
    private BigDecimal compareAtPrice;

    @Schema(description = "Currency code (ISO 4217)", example = "USD")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
    private String currency;

    @Schema(description = "Featured product flag", example = "true")
    private Boolean featured;

    // Constructors
    public UpdateProductRequest() {
    }

    public UpdateProductRequest(String name, String description, String brand,
            BigDecimal basePrice, BigDecimal compareAtPrice,
            String currency, Boolean featured) {
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.basePrice = basePrice;
        this.compareAtPrice = compareAtPrice;
        this.currency = currency;
        this.featured = featured;
    }

    // Getters and Setters
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

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }
}
