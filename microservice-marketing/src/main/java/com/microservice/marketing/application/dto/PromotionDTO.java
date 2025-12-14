package com.microservice.marketing.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PromotionDTO(
        Long id,
        String name,
        String description,
        String promotionType,
        BigDecimal discountValue,
        BigDecimal maxDiscountAmount,
        BigDecimal minPurchaseAmount,
        String appliesTo,
        String externalApplicableProductId,
        String externalApplicableCategoryId,
        String externalApplicableStoreId,
        Integer usageLimit,
        Integer usageCount,
        Integer perUserLimit,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Boolean isActive) {
}
