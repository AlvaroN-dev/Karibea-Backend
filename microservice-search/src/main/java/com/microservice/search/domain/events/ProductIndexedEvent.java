package com.microservice.search.domain.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento emitido cuando un producto ha sido indexado exitosamente.
 */
public record ProductIndexedEvent(
        UUID eventId,
        UUID productId,
        Instant occurredAt) implements DomainEvent {

    public ProductIndexedEvent {
        if (eventId == null) eventId = UUID.randomUUID();
        if (occurredAt == null) occurredAt = Instant.now();
    }

    public static ProductIndexedEvent of(UUID productId) {
        return new ProductIndexedEvent(UUID.randomUUID(), productId, Instant.now());
    }

    @Override
    public String eventType() {
        return "ProductIndexed";
    }
}
