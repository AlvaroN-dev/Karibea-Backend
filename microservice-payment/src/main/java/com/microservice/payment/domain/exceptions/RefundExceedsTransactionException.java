package com.microservice.payment.domain.exceptions;

import java.util.UUID;

import com.microservice.payment.domain.models.Money;

/**
 * Exception thrown when a refund amount exceeds the remaining refundable
 * amount.
 */
public class RefundExceedsTransactionException extends DomainException {

    public static final String CODE = "PAYMENT-003";

    private final Money requestedAmount;
    private final Money availableAmount;
    private final UUID transactionId;

    public RefundExceedsTransactionException(Money requestedAmount, Money availableAmount, UUID transactionId) {
        super(CODE, String.format(
                "Refund amount %s exceeds available refundable amount %s for transaction %s",
                requestedAmount, availableAmount, transactionId));
        this.requestedAmount = requestedAmount;
        this.availableAmount = availableAmount;
        this.transactionId = transactionId;
    }

    public Money getRequestedAmount() {
        return requestedAmount;
    }

    public Money getAvailableAmount() {
        return availableAmount;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
}
