package com.microservice.payment.domain.models.enums;

/**
 * Value Object representing the status of a refund request.
 * Separate from TransactionStatus as refunds have their own lifecycle.
 */
public enum RefundStatus {

    PENDING("Refund request created, awaiting processing"),
    APPROVED("Refund approved, awaiting execution"),
    PROCESSING("Refund is being processed"),
    COMPLETED("Refund completed successfully"),
    REJECTED("Refund request was rejected"),
    FAILED("Refund processing failed");

    private final String description;

    RefundStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Checks if this status represents a terminal state.
     */
    public boolean isTerminal() {
        return this == COMPLETED || this == REJECTED || this == FAILED;
    }

    /**
     * Checks if this status indicates successful completion.
     */
    public boolean isSuccessful() {
        return this == COMPLETED;
    }

    /**
     * Checks if the refund can still be cancelled.
     */
    public boolean isCancellable() {
        return this == PENDING || this == APPROVED;
    }
}
