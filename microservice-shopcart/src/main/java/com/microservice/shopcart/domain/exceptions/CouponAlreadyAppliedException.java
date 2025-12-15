package com.microservice.shopcart.domain.exceptions;

/**
 * Exception thrown when a coupon has already been applied to a cart.
 */
public class CouponAlreadyAppliedException extends RuntimeException {
    
    private final String couponCode;
    
    public CouponAlreadyAppliedException(String couponCode) {
        super(String.format("Coupon '%s' has already been applied to this cart", couponCode));
        this.couponCode = couponCode;
    }
    
    public String getCouponCode() {
        return couponCode;
    }
}
