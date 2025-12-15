package com.microservice.marketing.infrastructure.adapter.mapper;

import com.microservice.marketing.domain.model.Promotion;
import com.microservice.marketing.infrastructure.entities.PromotionEntity;
import org.springframework.stereotype.Component;

@Component
public class PromotionEntityMapper {

    public Promotion toDomain(PromotionEntity entity) {
        if (entity == null)
            return null;
        return new Promotion(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPromotionType(),
                entity.getDiscountValue(),
                entity.getMaxDiscountAmount(),
                entity.getMinPurchaseAmount(),
                entity.getAppliesTo(),
                entity.getExternalApplicableProductId(),
                entity.getExternalApplicableCategoryId(),
                entity.getExternalApplicableStoreId(),
                entity.getUsageLimit(),
                entity.getUsageCount(),
                entity.getPerUserLimit(),
                entity.getStartedAt(),
                entity.getEndedAt(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public PromotionEntity toEntity(Promotion domain) {
        if (domain == null)
            return null;
        PromotionEntity entity = new PromotionEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setPromotionType(domain.getPromotionType());
        entity.setDiscountValue(domain.getDiscountValue());
        entity.setMaxDiscountAmount(domain.getMaxDiscountAmount());
        entity.setMinPurchaseAmount(domain.getMinPurchaseAmount());
        entity.setAppliesTo(domain.getAppliesTo());
        entity.setExternalApplicableProductId(domain.getExternalApplicableProductId());
        entity.setExternalApplicableCategoryId(domain.getExternalApplicableCategoryId());
        entity.setExternalApplicableStoreId(domain.getExternalApplicableStoreId());
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
