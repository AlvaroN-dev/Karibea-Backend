package com.microservice.shopcart.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for adding an item to a shopping cart.
 */
@Schema(description = "Request to add an item to the shopping cart")
public class AddItemRequest {

    @Schema(description = "External product ID (UUID) from Catalog service", 
            example = "550e8400-e29b-41d4-a716-446655440001",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Product ID is required")
    private UUID productId;

    @Schema(description = "External variant ID (UUID) from Catalog service", 
            example = "550e8400-e29b-41d4-a716-446655440002",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private UUID variantId;

    @Schema(description = "External store ID (UUID) from Store service", 
            example = "550e8400-e29b-41d4-a716-446655440003",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Store ID is required")
    private UUID storeId;

    @Schema(description = "Quantity of items to add", 
            example = "2",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1",
            maximum = "999")
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 999, message = "Quantity cannot exceed 999")
    private Integer quantity;

    // Constructors
    public AddItemRequest() {
    }

    public AddItemRequest(UUID productId, UUID variantId, UUID storeId, Integer quantity) {
        this.productId = productId;
        this.variantId = variantId;
        this.storeId = storeId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getVariantId() {
        return variantId;
    }

    public void setVariantId(UUID variantId) {
        this.variantId = variantId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
