package com.microservice.marketing.infrastructure.adapter.mapper;

import com.microservice.marketing.domain.model.Coupon;
import com.microservice.marketing.infrastructure.entities.CouponEntity;
import com.microservice.marketing.infrastructure.entities.PromotionEntity;
import org.springframework.stereotype.Component;

@Component
public class CouponEntityMapper {

    public Coupon toDomain(CouponEntity entity) {
        if (entity == null)
            return null;
        return new Coupon(
                entity.getId(),
                entity.getPromotion() != null ? entity.getPromotion().getId() : null,
                entity.getCode(),
                entity.getUsageLimit(),
                entity.getUsageCount(),
                entity.getPerUserLimit(),
                entity.getStartedAt(),
                entity.getEndedAt(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public CouponEntity toEntity(Coupon domain) {
        if (domain == null)
            return null;
        CouponEntity entity = new CouponEntity();
        entity.setId(domain.getId());

        if (domain.getPromotionId() != null) {
            PromotionEntity promo = new PromotionEntity();
            promo.setId(domain.getPromotionId());
            entity.setPromotion(promo);
        }

        entity.setCode(domain.getCode());
        entity.setUsageLimit(domain.getUsageLimit());
        entity.setUsageCount(domain.getUsageCount());
        entity.setPerUserLimit(domain.getPerUserLimit());
        entity.setStartedAt(domain.getStartedAt());
        entity.setEndedAt(domain.getEndedAt());
        entity.setIsActive(domain.getIsActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
