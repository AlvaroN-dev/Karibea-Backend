package com.microservice.inventory.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base interface for all domain events.
 */
public interface DomainEvent {
    UUID getEventId();

    String getEventType();

    LocalDateTime getOccurredAt();

    UUID getAggregateId();
}
