package com.microservice.payment.application.exception;

import java.util.Map;

/**
 * Exception thrown when validation fails at the application layer.
 */
public class ValidationException extends ApplicationException {

    public static final String CODE = "VALIDATION_ERROR";

    private final Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super(CODE, message);
        this.fieldErrors = Map.of();
    }

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(CODE, message);
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
