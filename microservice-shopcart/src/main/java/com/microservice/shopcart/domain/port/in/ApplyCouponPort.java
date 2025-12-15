package com.microservice.shopcart.domain.port.in;

import java.util.UUID;

/**
 * Input port for applying a coupon to a shopping cart.
 */
public interface ApplyCouponPort {
    
    /**
     * Applies a coupon to the cart.
     *
     * @param cartId The shopping cart ID
     * @param couponCode The coupon code to apply
     */
    void execute(UUID cartId, String couponCode);
}
