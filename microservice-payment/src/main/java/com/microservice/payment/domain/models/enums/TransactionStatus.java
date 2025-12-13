package com.microservice.payment.domain.models.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Value Object representing the lifecycle status of a transaction.
 * Implements a state machine with valid transitions.
 * 
 * <p>
 * State transitions:
 * </p>
 * 
 * <pre>
 * PENDING → PROCESSING → COMPLETED
 *                     ↘ FAILED
 *                     ↘ CANCELLED
 * COMPLETED → PARTIALLY_REFUNDED → REFUNDED
 * </pre>
 */
public enum TransactionStatus {

    PENDING("Transaction created, awaiting processing"),
    PROCESSING("Transaction is being processed by payment provider"),
    COMPLETED("Transaction completed successfully"),
    FAILED("Transaction failed"),
    CANCELLED("Transaction was cancelled"),
    PARTIALLY_REFUNDED("Transaction has been partially refunded"),
    REFUNDED("Transaction has been fully refunded");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Validates if transition to target status is allowed.
     * Enforces business rules for transaction lifecycle.
     * 
     * @param target the target status
     * @return true if transition is valid
     */
    public boolean canTransitionTo(TransactionStatus target) {
        if (target == null) {
            return false;
        }
        return getAllowedTransitions().contains(target);
    }

    /**
     * Returns the set of valid target statuses from this status.
     */
    public Set<TransactionStatus> getAllowedTransitions() {
        return switch (this) {
            case PENDING -> Set.of(PROCESSING, CANCELLED);
            case PROCESSING -> Set.of(COMPLETED, FAILED, CANCELLED);
            case COMPLETED -> Set.of(PARTIALLY_REFUNDED, REFUNDED);
            case PARTIALLY_REFUNDED -> Set.of(REFUNDED);
            case FAILED, CANCELLED, REFUNDED -> Set.of();
        };
    }

    /**
     * Checks if this status represents a terminal state (no further transitions).
     */
    public boolean isTerminal() {
        return getAllowedTransitions().isEmpty();
    }

    /**
     * Checks if this status indicates the transaction was successful.
     */
    public boolean isSuccessful() {
        return this == COMPLETED || this == PARTIALLY_REFUNDED || this == REFUNDED;
    }

    /**
     * Checks if refunds are allowed for transactions in this status.
     */
    public boolean allowsRefund() {
        return this == COMPLETED || this == PARTIALLY_REFUNDED;
    }

    public static Set<String> allNames() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }
}
