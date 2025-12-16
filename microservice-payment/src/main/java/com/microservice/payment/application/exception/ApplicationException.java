package com.microservice.payment.application.exception;

/**
 * Base exception for application layer errors.
 * Application exceptions represent orchestration/coordination failures.
 */
public class ApplicationException extends RuntimeException {

    private final String code;

    public ApplicationException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ApplicationException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
