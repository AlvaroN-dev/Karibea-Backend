package com.microservice.shopcart.application.exception;

/**
 * Exception thrown when a cart operation fails.
 */
public class CartOperationException extends RuntimeException {

    public CartOperationException(String message) {
        super(message);
    }

    public CartOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
