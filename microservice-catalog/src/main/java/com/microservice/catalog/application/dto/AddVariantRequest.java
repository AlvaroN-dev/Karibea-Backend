package com.microservice.catalog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Request DTO for adding a variant to a product.
 */
@Schema(description = "Request payload for adding a variant to a product")
public class AddVariantRequest {

    @Schema(description = "Variant SKU", example = "PROD-001-M-RED", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "SKU is required")
    @Size(min = 3, max = 100, message = "SKU must be between 3 and 100 characters")
    private String sku;

    @Schema(description = "Variant name", example = "Medium / Red", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @Schema(description = "Variant price", example = "29.99", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @Schema(description = "Compare at price", example = "39.99")
    private BigDecimal compareAtPrice;

    @Schema(description = "Barcode", example = "1234567890123")
    @Size(max = 100, message = "Barcode cannot exceed 100 characters")
    private String barcode;

    // Constructors
    public AddVariantRequest() {
    }

    public AddVariantRequest(String sku, String name, BigDecimal price,
            BigDecimal compareAtPrice, String barcode) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.compareAtPrice = compareAtPrice;
        this.barcode = barcode;
    }

    // Getters and Setters
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
}
