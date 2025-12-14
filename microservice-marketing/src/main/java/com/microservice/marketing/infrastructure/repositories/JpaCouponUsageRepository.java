package com.microservice.marketing.infrastructure.repositories;

import com.microservice.marketing.infrastructure.entities.CouponUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaCouponUsageRepository extends JpaRepository<CouponUsageEntity, Long> {
    List<CouponUsageEntity> findByCouponId(Long couponId);

    List<CouponUsageEntity> findByExternalUserProfileId(String externalUserProfileId);
}
