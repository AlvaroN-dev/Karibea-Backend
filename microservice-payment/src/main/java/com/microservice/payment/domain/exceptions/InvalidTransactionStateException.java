package com.microservice.payment.domain.exceptions;

/**
 * Exception thrown when an invalid transaction state transition is attempted.
 */
public class InvalidTransactionStateException extends DomainException {

    public static final String CODE = "PAYMENT-001";

    public InvalidTransactionStateException(String message) {
        super(CODE, message);
    }

    public InvalidTransactionStateException(String message, Throwable cause) {
        super(CODE, message, cause);
    }
}
