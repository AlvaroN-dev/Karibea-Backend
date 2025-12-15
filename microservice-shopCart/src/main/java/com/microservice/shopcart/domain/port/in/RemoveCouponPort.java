package com.microservice.shopcart.domain.port.in;

import java.util.UUID;

/**
 * Input port for removing a coupon from a shopping cart.
 */
public interface RemoveCouponPort {
    
    /**
     * Removes a coupon from the cart.
     *
     * @param cartId The shopping cart ID
     * @param couponId The coupon ID to remove
     */
    void execute(UUID cartId, UUID couponId);
}
