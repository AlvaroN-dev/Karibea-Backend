package com.microservice.payment.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.payment.domain.events.DomainEvent;
import com.microservice.payment.domain.models.Money;
import com.microservice.payment.domain.models.Transaction;
import com.microservice.payment.domain.port.in.CreateTransactionUseCase;
import com.microservice.payment.domain.port.out.EventPublisherPort;
import com.microservice.payment.domain.port.out.PaymentMethodRepositoryPort;
import com.microservice.payment.domain.port.out.TransactionRepositoryPort;
import com.microservice.payment.domain.exceptions.PaymentMethodNotFoundException;

/**
 * Implementation of CreateTransactionUseCase.
 * Orchestrates transaction creation workflow.
 * 
 * <p>
 * <b>Why use cases don't access infrastructure directly:</b>
 * </p>
 * <ul>
 * <li>Use cases depend on ports (interfaces), not implementations</li>
 * <li>Infrastructure details are injected via DI</li>
 * <li>Enables unit testing without database/messaging</li>
 * <li>Follows Dependency Inversion Principle</li>
 * </ul>
 */
@Service
@Transactional
public class CreateTransactionService implements CreateTransactionUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final PaymentMethodRepositoryPort paymentMethodRepository;
    private final EventPublisherPort eventPublisher;

    public CreateTransactionService(
            TransactionRepositoryPort transactionRepository,
            PaymentMethodRepositoryPort paymentMethodRepository,
            EventPublisherPort eventPublisher) {
        this.transactionRepository = transactionRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Transaction execute(CreateTransactionCommand command) {
        // Validate payment method exists
        paymentMethodRepository.findById(command.paymentMethodId())
                .orElseThrow(() -> new PaymentMethodNotFoundException(command.paymentMethodId()));

        // Create domain transaction
        Money amount = Money.of(command.amount(), command.currency());
        Transaction transaction = Transaction.create(
                command.externalOrderId(),
                command.externalUserId(),
                amount,
                command.type(),
                command.paymentMethodId());

        // Persist transaction
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Publish domain events
        for (DomainEvent event : savedTransaction.getDomainEvents()) {
            eventPublisher.publish(event);
        }
        savedTransaction.clearDomainEvents();

        return savedTransaction;
    }
}
