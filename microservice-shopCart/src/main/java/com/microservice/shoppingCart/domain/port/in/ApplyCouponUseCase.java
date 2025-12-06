package com.microservice.shoppingCart.domain.port.in;

import java.util.UUID;

import com.microservice.shoppingCart.domain.model.CouponApplied;

public interface ApplyCouponUseCase {
    void applyCoupon(UUID cartId, CouponApplied coupon);
}
