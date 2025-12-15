package com.microservice.payment.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a transaction is not found.
 */
public class TransactionNotFoundException extends DomainException {

    public static final String CODE = "PAYMENT-004";

    private final UUID transactionId;

    public TransactionNotFoundException(UUID transactionId) {
        super(CODE, String.format("Transaction not found with ID: %s", transactionId));
        this.transactionId = transactionId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
}
