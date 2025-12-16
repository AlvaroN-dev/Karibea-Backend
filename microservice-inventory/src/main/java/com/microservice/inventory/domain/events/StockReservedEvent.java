package com.microservice.inventory.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

import com.microservice.inventory.domain.models.enums.ReservationType;

public record StockReservedEvent(
        UUID eventId,
        String eventType,
        LocalDateTime occurredAt,
        UUID aggregateId,
        UUID reservationId,
        UUID externalVariantId,
        UUID warehouseId,
        int quantity,
        ReservationType reservationType,
        UUID externalCartId,
        UUID externalOrderId) implements DomainEvent {

    public StockReservedEvent(UUID stockId, UUID reservationId, UUID externalVariantId,
            UUID warehouseId, int quantity, ReservationType reservationType,
            UUID externalCartId, UUID externalOrderId) {
        this(UUID.randomUUID(), "stock.reserved", LocalDateTime.now(),
                stockId, reservationId, externalVariantId, warehouseId,
                quantity, reservationType, externalCartId, externalOrderId);
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
