package com.microservice.shopcart.domain.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Event published when an item is removed from a shopping cart.
 */
public class ItemRemovedFromCartEvent implements DomainEvent {
    
    private final String eventId;
    private final UUID cartId;
    private final UUID itemId;
    private final UUID productId;
    private final Instant occurredAt;
    
    public ItemRemovedFromCartEvent(UUID cartId, UUID itemId, UUID productId, Instant occurredAt) {
        this.eventId = UUID.randomUUID().toString();
        this.cartId = cartId;
        this.itemId = itemId;
        this.productId = productId;
        this.occurredAt = occurredAt;
    }
    
    @Override
    public String getEventId() {
        return eventId;
    }
    
    @Override
    public String getEventType() {
        return "ItemRemovedFromCart";
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
    
    public UUID getItemId() {
        return itemId;
    }
    
    public UUID getProductId() {
        return productId;
    }
}
