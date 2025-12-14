package com.microservice.shopcart.domain.exceptions;

/**
 * Exception thrown when an invalid quantity is provided.
 */
public class InvalidQuantityException extends RuntimeException {
    
    private final int quantity;
    
    public InvalidQuantityException(String message) {
        super(message);
        this.quantity = 0;
    }
    
    public InvalidQuantityException(int quantity, String message) {
        super(message);
        this.quantity = quantity;
    }
    
    public int getQuantity() {
        return quantity;
    }
}
