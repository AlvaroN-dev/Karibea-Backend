package com.microservice.inventory.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record StockReleasedEvent(
        UUID eventId,
        String eventType,
        LocalDateTime occurredAt,
        UUID aggregateId,
        UUID reservationId,
        UUID externalVariantId,
        UUID warehouseId,
        int quantity,
        String reason) implements DomainEvent {

    public StockReleasedEvent(UUID stockId, UUID reservationId, UUID externalVariantId,
            UUID warehouseId, int quantity, String reason) {
        this(UUID.randomUUID(), "stock.released", LocalDateTime.now(),
                stockId, reservationId, externalVariantId, warehouseId, quantity, reason);
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }
}
