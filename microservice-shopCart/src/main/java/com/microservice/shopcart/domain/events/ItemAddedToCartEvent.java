package com.microservice.shopcart.domain.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Event published when an item is added to a shopping cart.
 */
public class ItemAddedToCartEvent implements DomainEvent {
    
    private final String eventId;
    private final UUID cartId;
    private final UUID itemId;
    private final UUID productId;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final Instant occurredAt;
    
    public ItemAddedToCartEvent(UUID cartId, UUID itemId, UUID productId, 
                                 int quantity, BigDecimal unitPrice, Instant occurredAt) {
        this.eventId = UUID.randomUUID().toString();
        this.cartId = cartId;
        this.itemId = itemId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.occurredAt = occurredAt;
    }
    
    @Override
    public String getEventId() {
        return eventId;
    }
    
    @Override
    public String getEventType() {
        return "ItemAddedToCart";
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
    
    public int getQuantity() {
        return quantity;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
}
