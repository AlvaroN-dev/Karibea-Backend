package com.microservice.shoppingCart.domain.port.out;

import java.util.Optional;

import com.microservice.shoppingCart.domain.model.CouponApplied;

public interface CouponRepositoryPort {
    CouponApplied save(CouponApplied coupon);
    Optional<CouponApplied> findByCode(String code);
}
