package com.microservice.shopcart.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for updating item quantity in a shopping cart.
 */
@Schema(description = "Request to update item quantity in the shopping cart")
public class UpdateItemQuantityRequest {

    @Schema(description = "New quantity for the item", 
            example = "3",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1",
            maximum = "999")
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 999, message = "Quantity cannot exceed 999")
    private Integer quantity;

    // Constructors
    public UpdateItemQuantityRequest() {
    }

    public UpdateItemQuantityRequest(Integer quantity) {
        this.quantity = quantity;
    }

    // Getters and Setters
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
