package com.microservice.payment.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.microservice.payment.domain.models.Refund;
import com.microservice.payment.domain.models.enums.RefundStatus;

/**
 * Port OUT - Repository contract for Refund persistence.
 */
public interface RefundRepositoryPort {

    /**
     * Saves a refund (insert or update).
     * 
     * @param refund the refund to save
     * @return the saved refund
     */
    Refund save(Refund refund);

    /**
     * Finds a refund by its ID.
     * 
     * @param id the refund ID
     * @return optional containing the refund if found
     */
    Optional<Refund> findById(UUID id);

    /**
     * Finds all refunds for a specific transaction.
     * 
     * @param transactionId the transaction ID
     * @return list of refunds for the transaction
     */
    List<Refund> findByTransactionId(UUID transactionId);

    /**
     * Finds all refunds by status.
     * 
     * @param status the refund status
     * @return list of refunds with the given status
     */
    List<Refund> findByStatus(RefundStatus status);
}
