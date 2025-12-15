package com.microservice.shopcart.domain.events;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Event published when a shopping cart expires due to inactivity.
 */
public class CartExpiredEvent implements DomainEvent {
    
    private final String eventId;
    private final UUID cartId;
    private final UUID userId;
    private final List<UUID> productIds;
    private final Instant occurredAt;
    
    public CartExpiredEvent(UUID cartId, UUID userId, List<UUID> productIds, Instant occurredAt) {
        this.eventId = UUID.randomUUID().toString();
        this.cartId = cartId;
        this.userId = userId;
        this.productIds = productIds;
        this.occurredAt = occurredAt;
    }
    
    @Override
    public String getEventId() {
        return eventId;
    }
    
    @Override
    public String getEventType() {
        return "CartExpired";
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
    
    public List<UUID> getProductIds() {
        return productIds;
    }
}
