package com.microservice.identity.domain.events;

import java.time.Instant;

/**
 * Base interface for all domain events.
 * Provides common contract for event identification, typing, and tracing.
 * 
 * All domain events should implement this interface to ensure:
 * - Unique event identification for idempotency
 * - Event type classification for routing
 * - Timestamp for ordering and auditing
 * - Aggregate identification for partitioning
 */
public interface DomainEvent {

    /**
     * Unique identifier for this event instance.
     * Used for idempotency checking and tracing.
     * 
     * @return Event ID (UUID)
     */
    String getEventId();

    /**
     * Type of the event (e.g., "UserCreated", "UserUpdated").
     * Used for event routing and deserialization.
     * 
     * @return Event type
     */
    String getEventType();

    /**
     * Timestamp when the event occurred.
     * Used for ordering and auditing.
     * 
     * @return Event timestamp
     */
    Instant getTimestamp();

    /**
     * Identifier of the aggregate root that generated this event.
     * Used for partition routing (events for same aggregate go to same partition).
     * 
     * @return Aggregate ID (e.g., userId)
     */
    String getAggregateId();
}
