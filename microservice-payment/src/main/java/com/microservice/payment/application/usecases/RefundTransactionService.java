package com.microservice.payment.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.payment.domain.events.DomainEvent;
import com.microservice.payment.domain.exceptions.TransactionNotFoundException;
import com.microservice.payment.domain.models.Money;
import com.microservice.payment.domain.models.Refund;
import com.microservice.payment.domain.models.Transaction;
import com.microservice.payment.domain.port.in.RefundTransactionUseCase;
import com.microservice.payment.domain.port.out.EventPublisherPort;
import com.microservice.payment.domain.port.out.RefundRepositoryPort;
import com.microservice.payment.domain.port.out.TransactionRepositoryPort;

/**
 * Implementation of RefundTransactionUseCase.
 * Orchestrates refund processing.
 */
@Service
@Transactional
public class RefundTransactionService implements RefundTransactionUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final RefundRepositoryPort refundRepository;
    private final EventPublisherPort eventPublisher;

    public RefundTransactionService(
            TransactionRepositoryPort transactionRepository,
            RefundRepositoryPort refundRepository,
            EventPublisherPort eventPublisher) {
        this.transactionRepository = transactionRepository;
        this.refundRepository = refundRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Refund execute(RefundTransactionCommand command) {
        // Fetch transaction
        Transaction transaction = transactionRepository.findById(command.transactionId())
                .orElseThrow(() -> new TransactionNotFoundException(command.transactionId()));

        // Process refund through domain (enforces invariants)
        Money refundAmount = Money.of(command.amount(), command.currency());
        Refund refund = transaction.processRefund(refundAmount, command.reason());

        // Persist changes
        transactionRepository.save(transaction);
        Refund savedRefund = refundRepository.save(refund);

        // Publish domain events
        for (DomainEvent event : transaction.getDomainEvents()) {
            eventPublisher.publish(event);
        }
        transaction.clearDomainEvents();

        return savedRefund;
    }
}
