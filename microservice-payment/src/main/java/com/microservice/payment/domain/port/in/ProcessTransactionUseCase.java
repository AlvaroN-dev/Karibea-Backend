package com.microservice.payment.domain.port.in;

import java.util.UUID;

import com.microservice.payment.domain.models.Transaction;

/**
 * Port IN - Use case for processing pending transactions.
 * Orchestrates the payment flow with external providers.
 */
public interface ProcessTransactionUseCase {

    /**
     * Processes a pending transaction.
     * 
     * @param command the command containing transaction ID and payment details
     * @return the processed transaction
     */
    Transaction execute(ProcessTransactionCommand command);

    /**
     * Command object for processing a transaction.
     */
    record ProcessTransactionCommand(
            UUID transactionId,
            String cardToken,
            String cvv) {
        public ProcessTransactionCommand {
            if (transactionId == null)
                throw new IllegalArgumentException("Transaction ID is required");
        }
    }
}
