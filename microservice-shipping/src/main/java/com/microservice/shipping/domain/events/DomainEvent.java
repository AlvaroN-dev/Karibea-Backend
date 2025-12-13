package com.microservice.shipping.domain.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Base class for all domain events.
 * PURE DOMAIN - No framework dependencies.
 */
public abstract class DomainEvent {

    private final UUID eventId;
    private final String eventType;
    private final UUID aggregateId;
    private final String aggregateType;
    private final Instant occurredOn;

    protected DomainEvent(UUID aggregateId, String eventType) {
        this.eventId = UUID.randomUUID();
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.aggregateType = "Shipment";
        this.occurredOn = Instant.now();
    }

    public UUID getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }
}
