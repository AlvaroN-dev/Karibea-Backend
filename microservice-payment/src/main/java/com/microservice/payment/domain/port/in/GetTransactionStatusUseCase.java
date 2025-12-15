package com.microservice.payment.domain.port.in;

import java.util.List;
import java.util.UUID;

import com.microservice.payment.domain.models.Transaction;

/**
 * Port IN - Use case for querying transaction status.
 * Provides read operations for transactions.
 */
public interface GetTransactionStatusUseCase {

    /**
     * Gets a transaction by its ID.
     * 
     * @param transactionId the transaction ID
     * @return the transaction
     */
    Transaction getById(UUID transactionId);

    /**
     * Gets all transactions for a specific order.
     * 
     * @param externalOrderId the external order ID
     * @return list of transactions for the order
     */
    List<Transaction> getByOrderId(UUID externalOrderId);

    /**
     * Gets all transactions for a specific user.
     * 
     * @param externalUserId the external user ID
     * @return list of transactions for the user
     */
    List<Transaction> getByUserId(UUID externalUserId);
}
