package com.microservice.shopcart.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when an item is not found in a shopping cart.
 */
public class ItemNotFoundException extends RuntimeException {
    
    private final UUID itemId;
    
    public ItemNotFoundException(UUID itemId) {
        super(String.format("Item with ID '%s' not found in cart", itemId));
        this.itemId = itemId;
    }
    
    public ItemNotFoundException(String message) {
        super(message);
        this.itemId = null;
    }
    
    public UUID getItemId() {
        return itemId;
    }
}
