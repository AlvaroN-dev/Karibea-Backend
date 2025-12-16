package com.microservice.notification.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base interface for all domain events in the notification service.
 * Domain events represent something that happened in the domain that domain experts care about.
 */
public interface DomainEvent {

    /**
     * Returns the unique identifier of this event.
     *
     * @return the event ID
     */
    UUID getEventId();

    /**
     * Returns the type of this event (e.g., "NotificationCreated").
     *
     * @return the event type as a string
     */
    String getEventType();

    /**
     * Returns the timestamp when this event occurred.
     *
     * @return the timestamp of occurrence
     */
    LocalDateTime getOccurredAt();

    /**
     * Returns the ID of the aggregate root that this event is associated with.
     *
     * @return the aggregate ID
     */
    UUID getAggregateId();
}
