package com.microservice.payment.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.microservice.payment.domain.models.Transaction;
import com.microservice.payment.domain.models.enums.TransactionStatus;

/**
 * Port OUT - Repository contract for Transaction persistence.
 * 
 * <p>
 * <b>Why repository ports in domain:</b>
 * </p>
 * <ul>
 * <li>Domain defines what it needs, not how it's done</li>
 * <li>Infrastructure provides the implementation</li>
 * <li>Enables testing domain without database</li>
 * <li>Allows switching persistence technology without domain changes</li>
 * </ul>
 */
public interface TransactionRepositoryPort {

    /**
     * Saves a transaction (insert or update).
     * 
     * @param transaction the transaction to save
     * @return the saved transaction
     */
    Transaction save(Transaction transaction);

    /**
     * Finds a transaction by its ID.
     * 
     * @param id the transaction ID
     * @return optional containing the transaction if found
     */
    Optional<Transaction> findById(UUID id);

    /**
     * Finds all transactions for a specific order.
     * 
     * @param externalOrderId the external order ID
     * @return list of transactions for the order
     */
    List<Transaction> findByExternalOrderId(UUID externalOrderId);

    /**
     * Finds all transactions for a specific user.
     * 
     * @param externalUserId the external user ID
     * @return list of transactions for the user
     */
    List<Transaction> findByExternalUserId(UUID externalUserId);

    /**
     * Finds all transactions by status.
     * 
     * @param status the transaction status
     * @return list of transactions with the given status
     */
    List<Transaction> findByStatus(TransactionStatus status);

    /**
     * Checks if a transaction exists.
     * 
     * @param id the transaction ID
     * @return true if exists
     */
    boolean existsById(UUID id);
}
