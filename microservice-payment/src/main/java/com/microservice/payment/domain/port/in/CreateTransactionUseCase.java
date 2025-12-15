package com.microservice.payment.domain.port.in;

import java.math.BigDecimal;
import java.util.UUID;

import com.microservice.payment.domain.models.Transaction;
import com.microservice.payment.domain.models.enums.TransactionType;

/**
 * Port IN - Use case for creating new transactions.
 * 
 * <p>
 * <b>Why ports live in domain:</b>
 * </p>
 * <ul>
 * <li>Ports define contracts that the domain needs</li>
 * <li>Infrastructure implements these contracts</li>
 * <li>Dependency Inversion: domain defines, infra implements</li>
 * </ul>
 */
public interface CreateTransactionUseCase {

    /**
     * Creates a new transaction.
     * 
     * @param command the command containing transaction details
     * @return the created transaction
     */
    Transaction execute(CreateTransactionCommand command);

    /**
     * Command object for creating a transaction.
     * Immutable record to ensure data integrity.
     */
    record CreateTransactionCommand(
            UUID externalOrderId,
            UUID externalUserId,
            BigDecimal amount,
            String currency,
            TransactionType type,
            UUID paymentMethodId) {
        public CreateTransactionCommand {
            if (externalOrderId == null)
                throw new IllegalArgumentException("External Order ID is required");
            if (externalUserId == null)
                throw new IllegalArgumentException("External User ID is required");
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalArgumentException("Amount must be positive");
            if (currency == null || currency.isBlank())
                throw new IllegalArgumentException("Currency is required");
            if (type == null)
                throw new IllegalArgumentException("Transaction type is required");
            if (paymentMethodId == null)
                throw new IllegalArgumentException("Payment method ID is required");
        }
    }
}
