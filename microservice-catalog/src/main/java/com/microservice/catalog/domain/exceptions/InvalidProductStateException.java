package com.microservice.catalog.domain.exceptions;

/**
 * Exception thrown when an invalid product state transition is attempted.
 */
public class InvalidProductStateException extends DomainException {

    private static final String ERROR_CODE = "INVALID_PRODUCT_STATE";

    public InvalidProductStateException(String message) {
        super(message, ERROR_CODE);
    }
}
