package com.microservice.payment.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

import com.microservice.payment.domain.models.Money;

/**
 * Event published when a refund is processed.
 * Used to notify inventory and order services about refunds.
 */
public record RefundProcessedEvent(
        UUID eventId,
        UUID refundId,
        UUID transactionId,
        UUID externalOrderId,
        Money amount,
        String reason,
        LocalDateTime occurredAt) implements DomainEvent {

    public static final String EVENT_TYPE = "payment.refund.processed";

    public RefundProcessedEvent(UUID refundId, UUID transactionId,
            UUID externalOrderId, Money amount,
            String reason, LocalDateTime occurredAt) {
        this(UUID.randomUUID(), refundId, transactionId,
                externalOrderId, amount, reason, occurredAt);
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
