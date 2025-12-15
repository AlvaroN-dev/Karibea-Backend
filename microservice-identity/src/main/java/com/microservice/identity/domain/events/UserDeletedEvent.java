package com.microservice.identity.domain.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event published when a user is deleted.
 * Contains minimal information as the user no longer exists.
 * 
 * This event is published to the user-deleted-events topic and can be consumed
 * by:
 * - Cache service (to remove user from cache)
 * - Analytics service (to track user churn)
 * - Cleanup service (to remove user data from other services)
 */
public class UserDeletedEvent implements DomainEvent {

    private static final String EVENT_TYPE = "UserDeleted";

    private final String eventId;
    private final Instant timestamp;
    private final String userId;
    private final String reason;

    /**
     * Constructor for UserDeletedEvent.
     * 
     * @param userId User ID
     * @param reason Reason for deletion (optional)
     */
    public UserDeletedEvent(String userId, String reason) {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.userId = userId;
        this.reason = reason;
    }

    /**
     * Constructor for UserDeletedEvent without reason.
     * 
     * @param userId User ID
     */
    public UserDeletedEvent(String userId) {
        this(userId, null);
    }

    /**
     * No-args constructor for JSON deserialization.
     */
    public UserDeletedEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.userId = null;
        this.reason = null;
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

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "UserDeletedEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventType='" + EVENT_TYPE + '\'' +
                ", timestamp=" + timestamp +
                ", userId='" + userId + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
