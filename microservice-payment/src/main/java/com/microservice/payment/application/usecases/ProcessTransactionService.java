package com.microservice.payment.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.payment.application.exception.PaymentProviderException;
import com.microservice.payment.domain.events.DomainEvent;
import com.microservice.payment.domain.exceptions.TransactionNotFoundException;
import com.microservice.payment.domain.models.Transaction;
import com.microservice.payment.domain.port.in.ProcessTransactionUseCase;
import com.microservice.payment.domain.port.out.EventPublisherPort;
import com.microservice.payment.domain.port.out.PaymentProviderPort;
import com.microservice.payment.domain.port.out.PaymentProviderPort.PaymentRequest;
import com.microservice.payment.domain.port.out.PaymentProviderPort.PaymentResult;
import com.microservice.payment.domain.port.out.TransactionRepositoryPort;

/**
 * Implementation of ProcessTransactionUseCase.
 * Orchestrates payment processing with external provider.
 */
@Service
@Transactional
public class ProcessTransactionService implements ProcessTransactionUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final PaymentProviderPort paymentProvider;
    private final EventPublisherPort eventPublisher;

    public ProcessTransactionService(
            TransactionRepositoryPort transactionRepository,
            PaymentProviderPort paymentProvider,
            EventPublisherPort eventPublisher) {
        this.transactionRepository = transactionRepository;
        this.paymentProvider = paymentProvider;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Transaction execute(ProcessTransactionCommand command) {
        // Fetch transaction
        Transaction transaction = transactionRepository.findById(command.transactionId())
                .orElseThrow(() -> new TransactionNotFoundException(command.transactionId()));

        // Start processing
        transaction.startProcessing();
        transactionRepository.save(transaction);

        // Process with external provider
        PaymentRequest paymentRequest = new PaymentRequest(
                transaction,
                command.cardToken(),
                command.cvv());

        try {
            PaymentResult result = paymentProvider.processPayment(paymentRequest);

            if (result.success()) {
                transaction.complete(result.providerTransactionId());
            } else {
                transaction.fail(result.errorMessage());
            }
        } catch (Exception e) {
            transaction.fail("Payment provider error: " + e.getMessage());
            throw new PaymentProviderException("Failed to process payment", e);
        }

        // Persist updated transaction
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Publish domain events
        for (DomainEvent event : savedTransaction.getDomainEvents()) {
            eventPublisher.publish(event);
        }
        savedTransaction.clearDomainEvents();

        return savedTransaction;
    }
}
