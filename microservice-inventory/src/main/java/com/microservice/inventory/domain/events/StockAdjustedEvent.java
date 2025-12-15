package com.microservice.inventory.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

import com.microservice.inventory.domain.models.enums.MovementType;

public record StockAdjustedEvent(
        UUID eventId,
        String eventType,
        LocalDateTime occurredAt,
        UUID aggregateId,
        UUID externalVariantId,
        UUID warehouseId,
        MovementType movementType,
        int quantity,
        int newQuantityAvailable,
        String referenceType) implements DomainEvent {

    public StockAdjustedEvent(UUID stockId, UUID externalVariantId, UUID warehouseId,
            MovementType movementType, int quantity,
            int newQuantityAvailable, String referenceType) {
        this(UUID.randomUUID(), "stock.adjusted", LocalDateTime.now(),
                stockId, externalVariantId, warehouseId, movementType,
                quantity, newQuantityAvailable, referenceType);
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
