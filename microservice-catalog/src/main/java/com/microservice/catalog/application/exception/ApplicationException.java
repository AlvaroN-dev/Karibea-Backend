package com.microservice.catalog.application.exception;

/**
 * Base exception for application-level errors.
 * These are errors that occur during use case execution.
 */
public abstract class ApplicationException extends RuntimeException {

    private final String errorCode;

    protected ApplicationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    protected ApplicationException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
