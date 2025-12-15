package com.microservice.shopcart.domain.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Event published when a coupon is applied to a shopping cart.
 */
public class CouponAppliedToCartEvent implements DomainEvent {
    
    private final String eventId;
    private final UUID cartId;
    private final String couponCode;
    private final BigDecimal discountAmount;
    private final Instant occurredAt;
    
    public CouponAppliedToCartEvent(UUID cartId, String couponCode, 
                                     BigDecimal discountAmount, Instant occurredAt) {
        this.eventId = UUID.randomUUID().toString();
        this.cartId = cartId;
        this.couponCode = couponCode;
        this.discountAmount = discountAmount;
        this.occurredAt = occurredAt;
    }
    
    @Override
    public String getEventId() {
        return eventId;
    }
    
    @Override
    public String getEventType() {
        return "CouponAppliedToCart";
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
    
    public String getCouponCode() {
        return couponCode;
    }
    
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
}
