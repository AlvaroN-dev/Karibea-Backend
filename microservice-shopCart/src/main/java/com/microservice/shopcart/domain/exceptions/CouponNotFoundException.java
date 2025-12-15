package com.microservice.shopcart.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a coupon is not found in a cart.
 */
public class CouponNotFoundException extends RuntimeException {
    
    private final UUID couponId;
    
    public CouponNotFoundException(UUID couponId) {
        super(String.format("Coupon with ID '%s' not found in cart", couponId));
        this.couponId = couponId;
    }
    
    public CouponNotFoundException(String message) {
        super(message);
        this.couponId = null;
    }
    
    public UUID getCouponId() {
        return couponId;
    }
}
