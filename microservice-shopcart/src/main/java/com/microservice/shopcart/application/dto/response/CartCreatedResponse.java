package com.microservice.shopcart.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Simple response for cart creation.
 */
@Schema(description = "Response after creating a shopping cart")
public class CartCreatedResponse {

    @Schema(description = "Created cart ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID cartId;

    @Schema(description = "Success message", example = "Shopping cart created successfully")
    private String message;

    // Constructors
    public CartCreatedResponse() {
    }

    public CartCreatedResponse(UUID cartId, String message) {
        this.cartId = cartId;
        this.message = message;
    }

    public static CartCreatedResponse of(UUID cartId) {
        return new CartCreatedResponse(cartId, "Shopping cart created successfully");
    }

    // Getters and Setters
    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
