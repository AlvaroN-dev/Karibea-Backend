package com.microservice.shopcart.domain.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Event published when a new shopping cart is created.
 */
public class CartCreatedEvent implements DomainEvent {
    
    private final String eventId;
    private final UUID cartId;
    private final UUID userId;
    private final String sessionId;
    private final Instant occurredAt;
    
    public CartCreatedEvent(UUID cartId, UUID userId, String sessionId, Instant occurredAt) {
        this.eventId = UUID.randomUUID().toString();
        this.cartId = cartId;
        this.userId = userId;
        this.sessionId = sessionId;
        this.occurredAt = occurredAt;
    }
    
    @Override
    public String getEventId() {
        return eventId;
    }
    
    @Override
    public String getEventType() {
        return "CartCreated";
    }
    
    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }
    
    @Override
    public String getAggregateId() {
        return cartId.toString();
    }
    
    public UUID getCartId() {
        return cartId;
    }
    
    public UUID getUserId() {
        return userId;
    }
    
    public String getSessionId() {
        return sessionId;
    }
}
