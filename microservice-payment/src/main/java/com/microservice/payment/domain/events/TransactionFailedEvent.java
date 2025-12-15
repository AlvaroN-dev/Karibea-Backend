package com.microservice.payment.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event published when a transaction fails.
 * Used to notify order service to handle failed payments.
 */
public record TransactionFailedEvent(
        UUID eventId,
        UUID transactionId,
        UUID externalOrderId,
        UUID externalUserId,
        String failureReason,
        LocalDateTime occurredAt) implements DomainEvent {

    public static final String EVENT_TYPE = "payment.transaction.failed";

    public TransactionFailedEvent(UUID transactionId, UUID externalOrderId,
            UUID externalUserId, String failureReason,
            LocalDateTime occurredAt) {
        this(UUID.randomUUID(), transactionId, externalOrderId,
                externalUserId, failureReason, occurredAt);
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
