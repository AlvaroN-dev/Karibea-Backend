package com.microservice.inventory.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record LowStockAlertEvent(
        UUID eventId,
        String eventType,
        LocalDateTime occurredAt,
        UUID aggregateId,
        UUID externalVariantId,
        UUID warehouseId,
        int currentQuantity,
        int threshold) implements DomainEvent {

    public LowStockAlertEvent(UUID stockId, UUID externalVariantId, UUID warehouseId,
            int currentQuantity, int threshold) {
        this(UUID.randomUUID(), "stock.low_alert", LocalDateTime.now(),
                stockId, externalVariantId, warehouseId, currentQuantity, threshold);
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
