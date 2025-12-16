package com.microservice.catalog.domain.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Base class for all domain events in the Catalog bounded context.
 * Domain events represent something that happened in the domain.
 */
public abstract class DomainEvent {

    private final UUID eventId;
    private final Instant occurredOn;
    private final String eventType;

    protected DomainEvent(String eventType) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = Instant.now();
        this.eventType = eventType;
    }

    public UUID getEventId() {
        return eventId;
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }

    public String getEventType() {
        return eventType;
    }

    /**
     * Returns the aggregate ID associated with this event.
     */
    public abstract UUID getAggregateId();
}
