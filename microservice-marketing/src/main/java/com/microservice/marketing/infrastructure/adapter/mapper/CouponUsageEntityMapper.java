package com.microservice.marketing.infrastructure.adapter.mapper;

import com.microservice.marketing.domain.model.CouponUsage;
import com.microservice.marketing.infrastructure.entities.CouponEntity;
import com.microservice.marketing.infrastructure.entities.CouponUsageEntity;
import org.springframework.stereotype.Component;

@Component
public class CouponUsageEntityMapper {

    public CouponUsage toDomain(CouponUsageEntity entity) {
        if (entity == null)
            return null;
        return new CouponUsage(
                entity.getId(),
                entity.getCoupon() != null ? entity.getCoupon().getId() : null,
                entity.getExternalUserProfileId(),
                entity.getExternalOrderId(),
                entity.getDiscountAmount(),
                entity.getUsedAt());
    }

    public CouponUsageEntity toEntity(CouponUsage domain) {
        if (domain == null)
            return null;
        CouponUsageEntity entity = new CouponUsageEntity();
        entity.setId(domain.getId());

        if (domain.getCouponId() != null) {
            CouponEntity coupon = new CouponEntity();
            coupon.setId(domain.getCouponId());
            entity.setCoupon(coupon);
        }

        entity.setExternalUserProfileId(domain.getExternalUserProfileId());
        entity.setExternalOrderId(domain.getExternalOrderId());
        entity.setDiscountAmount(domain.getDiscountAmount());
        entity.setUsedAt(domain.getUsedAt());
        return entity;
    }
}
