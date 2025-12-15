package com.microservice.identity.domain.events;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Domain event published when a new user is created.
 * Contains user information needed by other services (no sensitive data like
 * passwords).
 * 
 * This event is published to the user-created-events topic and can be consumed
 * by:
 * - Email service (to send welcome email)
 * - Analytics service (to track user registration)
 * - Notification service (to create user notifications)
 */
public class UserCreatedEvent implements DomainEvent {

    private static final String EVENT_TYPE = "UserCreated";

    private final String eventId;
    private final Instant timestamp;
    private final String userId;
    private final String username;
    private final String email;
    private final Set<String> roles;
    private final boolean enabled;

    /**
     * Constructor for UserCreatedEvent.
     * 
     * @param userId   User ID
     * @param username Username
     * @param email    User email
     * @param roles    User roles
     * @param enabled  Whether user is enabled
     */
    public UserCreatedEvent(String userId, String username, String email, Set<String> roles, boolean enabled) {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.enabled = enabled;
    }

    /**
     * No-args constructor for JSON deserialization.
     */
    public UserCreatedEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.userId = null;
        this.username = null;
        this.email = null;
        this.roles = null;
        this.enabled = false;
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

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "UserCreatedEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventType='" + EVENT_TYPE + '\'' +
                ", timestamp=" + timestamp +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", enabled=" + enabled +
                '}';
    }
}
