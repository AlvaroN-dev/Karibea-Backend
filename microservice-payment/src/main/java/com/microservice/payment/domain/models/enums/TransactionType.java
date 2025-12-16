package com.microservice.payment.domain.models.enums;

/**
 * Value Object representing the type of financial transaction.
 * Used to categorize transactions for reporting and business logic.
 */
public enum TransactionType {

    PAYMENT("Standard payment transaction"),
    REFUND("Full refund of a previous payment"),
    PARTIAL_REFUND("Partial refund of a previous payment"),
    CHARGEBACK("Disputed transaction reversed by bank"),
    AUTHORIZATION("Pre-authorization hold"),
    CAPTURE("Capture of previously authorized amount"),
    VOID("Cancellation of pending authorization");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Checks if this transaction type represents a credit (money returned).
     */
    public boolean isCredit() {
        return this == REFUND || this == PARTIAL_REFUND || this == CHARGEBACK;
    }

    /**
     * Checks if this transaction type represents a debit (money collected).
     */
    public boolean isDebit() {
        return this == PAYMENT || this == CAPTURE;
    }

    /**
     * Checks if this transaction type requires an existing parent transaction.
     */
    public boolean requiresParentTransaction() {
        return this == REFUND || this == PARTIAL_REFUND ||
                this == CAPTURE || this == VOID || this == CHARGEBACK;
    }
}
