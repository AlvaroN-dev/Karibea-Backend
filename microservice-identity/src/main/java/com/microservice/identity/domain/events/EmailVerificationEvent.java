package com.microservice.identity.domain.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event published when email verification is required.
 * Contains verification token and user information needed to send verification
 * email.
 * 
 * This event is published to the email-verification-events topic and consumed
 * by:
 * - Email service (to send verification email with token)
 */
public class EmailVerificationEvent implements DomainEvent {

    private static final String EVENT_TYPE = "EmailVerification";

    private final String eventId;
    private final Instant timestamp;
    private final String userId;
    private final String email;
    private final String verificationToken;
    private final Instant tokenExpiresAt;

    /**
     * Constructor for EmailVerificationEvent.
     * 
     * @param userId            User ID
     * @param email             User email
     * @param verificationToken Verification token
     * @param tokenExpiresAt    Token expiration timestamp
     */
    public EmailVerificationEvent(String userId, String email, String verificationToken, Instant tokenExpiresAt) {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.userId = userId;
        this.email = email;
        this.verificationToken = verificationToken;
        this.tokenExpiresAt = tokenExpiresAt;
    }

    /**
     * No-args constructor for JSON deserialization.
     */
    public EmailVerificationEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.userId = null;
        this.email = null;
        this.verificationToken = null;
        this.tokenExpiresAt = null;
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

    public String getEmail() {
        return email;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public Instant getTokenExpiresAt() {
        return tokenExpiresAt;
    }

    @Override
    public String toString() {
        return "EmailVerificationEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventType='" + EVENT_TYPE + '\'' +
                ", timestamp=" + timestamp +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", verificationToken='***'" + // Don't log token
                ", tokenExpiresAt=" + tokenExpiresAt +
                '}';
    }
}
