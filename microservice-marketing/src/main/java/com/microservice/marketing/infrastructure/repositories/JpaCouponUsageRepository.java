package com.microservice.marketing.infrastructure.repositories;

import com.microservice.marketing.infrastructure.entities.CouponUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaCouponUsageRepository extends JpaRepository<CouponUsageEntity, UUID> {
    List<CouponUsageEntity> findByCouponId(UUID couponId);

    List<CouponUsageEntity> findByExternalUserProfileId(String externalUserProfileId);
}
