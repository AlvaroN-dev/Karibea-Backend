package com.microservice.shopcart.domain.events;

import java.time.Instant;

/**
 * Base interface for all domain events in the Shopping Cart bounded context.
 */
public interface DomainEvent {
    
    /**
     * Returns the unique identifier of the event.
     */
    String getEventId();
    
    /**
     * Returns the type/name of the event.
     */
    String getEventType();
    
    /**
     * Returns when the event occurred.
     */
    Instant getOccurredAt();
    
    /**
     * Returns the aggregate ID this event belongs to.
     */
    String getAggregateId();
}
