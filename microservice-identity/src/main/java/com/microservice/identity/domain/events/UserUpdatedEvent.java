package com.microservice.identity.domain.events;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Domain event published when user information is updated.
 * Contains only the fields that were updated to minimize payload size.
 * 
 * This event is published to the user-updated-events topic and can be consumed
 * by:
 * - Cache service (to invalidate user cache)
 * - Analytics service (to track user profile changes)
 * - Audit service (to log user modifications)
 */
public class UserUpdatedEvent implements DomainEvent {

    private static final String EVENT_TYPE = "UserUpdated";

    private final String eventId;
    private final Instant timestamp;
    private final String userId;
    private final Map<String, Object> updatedFields;

    /**
     * Constructor for UserUpdatedEvent.
     * 
     * @param userId        User ID
     * @param updatedFields Map of field names to new values
     */
    public UserUpdatedEvent(String userId, Map<String, Object> updatedFields) {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.userId = userId;
        this.updatedFields = updatedFields;
    }

    /**
     * No-args constructor for JSON deserialization.
     */
    public UserUpdatedEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.userId = null;
        this.updatedFields = null;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String getAggregateId() {
        return getUserId();
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public Map<String, Object> getUpdatedFields() {
        return updatedFields;
    }

    @Override
    public String toString() {
        return "UserUpdatedEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventType='" + EVENT_TYPE + '\'' +
                ", timestamp=" + timestamp +
                ", userId='" + userId + '\'' +
                ", updatedFields=" + updatedFields +
                '}';
    }
}
