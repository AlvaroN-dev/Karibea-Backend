package com.microservice.shipping.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Response DTO containing enriched product information.
 * Represents external product data from the catalog service.
 */
@Schema(description = "Product information for shipment items")
public class ProductInfoResponse {

    @Schema(
            description = "Unique identifier of the product",
            example = "550e8400-e29b-41d4-a716-446655440010"
    )
    private UUID id;

    @Schema(
            description = "Product name",
            example = "Wireless Bluetooth Headphones"
    )
    private String name;

    @Schema(
            description = "Product description",
            example = "Premium wireless headphones with noise cancellation"
    )
    private String description;

    @Schema(
            description = "Product brand",
            example = "Sony"
    )
    private String brand;

    @Schema(
            description = "Stock keeping unit code",
            example = "WBH-001-BLK"
    )
    private String sku;

    @Schema(
            description = "Base price of the product",
            example = "99.99"
    )
    private BigDecimal basePrice;

    @Schema(
            description = "Primary image URL",
            example = "https://cdn.karibea.com/products/headphones-black.jpg"
    )
    private String primaryImageUrl;

    @Schema(
            description = "Weight in kilograms",
            example = "0.35"
    )
    private Double weightKg;

    // Constructors
    public ProductInfoResponse() {
    }

    public ProductInfoResponse(UUID id, String name, String description, String brand, 
                                String sku, BigDecimal basePrice, String primaryImageUrl, Double weightKg) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.sku = sku;
        this.basePrice = basePrice;
        this.primaryImageUrl = primaryImageUrl;
        this.weightKg = weightKg;
    }

    // Static factory for creating with just ID
    public static ProductInfoResponse withId(UUID id) {
        ProductInfoResponse response = new ProductInfoResponse();
        response.setId(id);
        return response;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public String getPrimaryImageUrl() {
        return primaryImageUrl;
    }

    public void setPrimaryImageUrl(String primaryImageUrl) {
        this.primaryImageUrl = primaryImageUrl;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }
}
