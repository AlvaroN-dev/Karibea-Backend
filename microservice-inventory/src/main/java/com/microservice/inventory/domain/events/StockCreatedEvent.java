package com.microservice.inventory.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record StockCreatedEvent(
        UUID eventId,
        String eventType,
        LocalDateTime occurredAt,
        UUID aggregateId,
        UUID externalProductId,
        UUID externalVariantId,
        UUID warehouseId,
        int initialQuantity) implements DomainEvent {

    public StockCreatedEvent(UUID stockId, UUID externalProductId, UUID externalVariantId,
            UUID warehouseId, int initialQuantity) {
        this(UUID.randomUUID(), "stock.created", LocalDateTime.now(),
                stockId, externalProductId, externalVariantId, warehouseId, initialQuantity);
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
