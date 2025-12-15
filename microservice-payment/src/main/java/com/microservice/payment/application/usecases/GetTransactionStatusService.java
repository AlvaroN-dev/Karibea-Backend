package com.microservice.payment.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.payment.domain.exceptions.TransactionNotFoundException;
import com.microservice.payment.domain.models.Transaction;
import com.microservice.payment.domain.port.in.GetTransactionStatusUseCase;
import com.microservice.payment.domain.port.out.TransactionRepositoryPort;

/**
 * Implementation of GetTransactionStatusUseCase.
 * Provides read operations for transactions.
 */
@Service
@Transactional(readOnly = true)
public class GetTransactionStatusService implements GetTransactionStatusUseCase {

    private final TransactionRepositoryPort transactionRepository;

    public GetTransactionStatusService(TransactionRepositoryPort transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction getById(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }

    @Override
    public List<Transaction> getByOrderId(UUID externalOrderId) {
        return transactionRepository.findByExternalOrderId(externalOrderId);
    }

    @Override
    public List<Transaction> getByUserId(UUID externalUserId) {
        return transactionRepository.findByExternalUserId(externalUserId);
    }
}
