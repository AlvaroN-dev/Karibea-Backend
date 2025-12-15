package com.microservice.payment.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

import com.microservice.payment.domain.models.Money;
import com.microservice.payment.domain.models.enums.TransactionType;

/**
 * Event published when a new transaction is created.
 * Used to notify other services about payment initiation.
 */
public record TransactionCreatedEvent(
        UUID eventId,
        UUID transactionId,
        UUID externalOrderId,
        UUID externalUserId,
        Money amount,
        TransactionType type,
        LocalDateTime occurredAt) implements DomainEvent {

    public static final String EVENT_TYPE = "payment.transaction.created";

    public TransactionCreatedEvent(UUID transactionId, UUID externalOrderId,
            UUID externalUserId, Money amount,
            TransactionType type, LocalDateTime occurredAt) {
        this(UUID.randomUUID(), transactionId, externalOrderId,
                externalUserId, amount, type, occurredAt);
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
