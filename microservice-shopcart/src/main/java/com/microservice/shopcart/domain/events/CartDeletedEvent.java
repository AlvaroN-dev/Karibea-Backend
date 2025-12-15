package com.microservice.shopcart.domain.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when a shopping cart is soft deleted.
 */
public class CartDeletedEvent implements DomainEvent {
    
    private final String eventId;
    private final String eventType = "CartDeleted";
    private final UUID cartId;
    private final UUID userProfileId;
    private final Instant occurredAt;

    public CartDeletedEvent(UUID cartId, UUID userProfileId, Instant occurredAt) {
        this.eventId = UUID.randomUUID().toString();
        this.cartId = cartId;
        this.userProfileId = userProfileId;
        this.occurredAt = occurredAt;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public String getAggregateId() {
        return cartId.toString();
    }

    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }

    public UUID getCartId() {
        return cartId;
    }

    public UUID getUserProfileId() {
        return userProfileId;
    }
}
