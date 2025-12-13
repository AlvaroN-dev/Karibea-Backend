package com.microservice.payment.domain.port.in;

import java.math.BigDecimal;
import java.util.UUID;

import com.microservice.payment.domain.models.Refund;

/**
 * Port IN - Use case for processing refunds.
 * Handles full and partial refunds for completed transactions.
 */
public interface RefundTransactionUseCase {

    /**
     * Processes a refund for a transaction.
     * 
     * @param command the command containing refund details
     * @return the created refund
     */
    Refund execute(RefundTransactionCommand command);

    /**
     * Command object for processing a refund.
     */
    record RefundTransactionCommand(
            UUID transactionId,
            BigDecimal amount,
            String currency,
            String reason) {
        public RefundTransactionCommand {
            if (transactionId == null)
                throw new IllegalArgumentException("Transaction ID is required");
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalArgumentException("Refund amount must be positive");
            if (currency == null || currency.isBlank())
                throw new IllegalArgumentException("Currency is required");
        }
    }
}
