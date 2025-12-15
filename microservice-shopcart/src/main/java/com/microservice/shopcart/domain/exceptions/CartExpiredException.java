package com.microservice.shopcart.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a shopping cart has expired.
 */
public class CartExpiredException extends RuntimeException {
    
    private final UUID cartId;
    
    public CartExpiredException(UUID cartId) {
        super(String.format("Shopping cart with ID '%s' has expired", cartId));
        this.cartId = cartId;
    }
    
    public CartExpiredException(String message) {
        super(message);
        this.cartId = null;
    }
    
    public UUID getCartId() {
        return cartId;
    }
}
