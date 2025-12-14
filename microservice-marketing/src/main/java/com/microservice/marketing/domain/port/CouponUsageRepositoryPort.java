package com.microservice.marketing.domain.port;

import com.microservice.marketing.domain.model.CouponUsage;
import java.util.List;

public interface CouponUsageRepositoryPort {
    CouponUsage save(CouponUsage couponUsage);

    List<CouponUsage> findByCouponId(Long couponId);

    List<CouponUsage> findByExternalUserProfileId(String userId);
}
