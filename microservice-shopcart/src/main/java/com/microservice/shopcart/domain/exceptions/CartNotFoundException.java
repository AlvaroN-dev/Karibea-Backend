package com.microservice.shopcart.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a shopping cart is not found.
 */
public class CartNotFoundException extends RuntimeException {
    
    private final UUID cartId;
    
    public CartNotFoundException(UUID cartId) {
        super(String.format("Shopping cart with ID '%s' not found", cartId));
        this.cartId = cartId;
    }
    
    public CartNotFoundException(String message) {
        super(message);
        this.cartId = null;
    }
    
    public UUID getCartId() {
        return cartId;
    }
}
