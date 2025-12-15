package com.microservice.payment.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base interface for all domain events.
 * Domain events represent something that happened in the domain
 * that domain experts care about.
 */
public interface DomainEvent {

    /**
     * Unique identifier for this event instance.
     */
    UUID getEventId();

    /**
     * Type name of the event for routing/serialization.
     */
    String getEventType();

    /**
     * When the event occurred.
     */
    LocalDateTime getOccurredAt();

    /**
     * Aggregate ID that generated this event.
     */
    UUID getAggregateId();
}
