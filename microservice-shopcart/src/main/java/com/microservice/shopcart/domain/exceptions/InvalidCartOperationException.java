package com.microservice.shopcart.domain.exceptions;

/**
 * Exception thrown when an invalid operation is attempted on a cart.
 */
public class InvalidCartOperationException extends RuntimeException {
    
    public InvalidCartOperationException(String message) {
        super(message);
    }
    
    public InvalidCartOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
