package com.microservice.marketing.domain.port;

import com.microservice.marketing.domain.model.Coupon;
import java.util.List;
import java.util.Optional;

public interface CouponRepositoryPort {
    Coupon save(Coupon coupon);

    Optional<Coupon> findById(Long id);

    Optional<Coupon> findByCode(String code);

    List<Coupon> findAll();

    void deleteById(Long id);
}
