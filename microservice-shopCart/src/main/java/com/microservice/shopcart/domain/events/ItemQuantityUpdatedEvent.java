package com.microservice.shopcart.domain.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Event published when an item quantity is updated in a shopping cart.
 */
public class ItemQuantityUpdatedEvent implements DomainEvent {
    
    private final String eventId;
    private final UUID cartId;
    private final UUID itemId;
    private final int newQuantity;
    private final Instant occurredAt;
    
    public ItemQuantityUpdatedEvent(UUID cartId, UUID itemId, int newQuantity, Instant occurredAt) {
        this.eventId = UUID.randomUUID().toString();
        this.cartId = cartId;
        this.itemId = itemId;
        this.newQuantity = newQuantity;
        this.occurredAt = occurredAt;
    }
    
    @Override
    public String getEventId() {
        return eventId;
    }
    
    @Override
    public String getEventType() {
        return "ItemQuantityUpdated";
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
    
    public int getNewQuantity() {
        return newQuantity;
    }
}
