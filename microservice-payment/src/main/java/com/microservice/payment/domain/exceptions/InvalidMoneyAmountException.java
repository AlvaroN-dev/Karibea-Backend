package com.microservice.payment.domain.exceptions;

/**
 * Exception thrown when an invalid money amount is provided.
 */
public class InvalidMoneyAmountException extends DomainException {

    public static final String CODE = "PAYMENT-002";

    public InvalidMoneyAmountException(String message) {
        super(CODE, message);
    }

    public InvalidMoneyAmountException(String message, Throwable cause) {
        super(CODE, message, cause);
    }
}
