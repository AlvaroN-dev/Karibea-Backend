package com.microservice.marketing.domain.port;

import com.microservice.marketing.domain.model.CouponUsage;
import java.util.List;
import java.util.UUID;

public interface CouponUsageRepositoryPort {
    CouponUsage save(CouponUsage couponUsage);

    List<CouponUsage> findByCouponId(UUID couponId);

    List<CouponUsage> findByExternalUserProfileId(String userId);
}
