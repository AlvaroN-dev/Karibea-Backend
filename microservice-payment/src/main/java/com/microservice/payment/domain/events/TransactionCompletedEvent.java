package com.microservice.payment.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

import com.microservice.payment.domain.models.Money;

/**
 * Event published when a transaction is successfully completed.
 * Critical for order fulfillment and inventory management.
 */
public record TransactionCompletedEvent(
        UUID eventId,
        UUID transactionId,
        UUID externalOrderId,
        UUID externalUserId,
        Money amount,
        String providerTransactionId,
        LocalDateTime occurredAt) implements DomainEvent {

    public static final String EVENT_TYPE = "payment.transaction.completed";

    public TransactionCompletedEvent(UUID transactionId, UUID externalOrderId,
            UUID externalUserId, Money amount,
            String providerTransactionId, LocalDateTime occurredAt) {
        this(UUID.randomUUID(), transactionId, externalOrderId,
                externalUserId, amount, providerTransactionId, occurredAt);
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    @Override
    public UUID getAggregateId() {
        return transactionId;
    }
}
