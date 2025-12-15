package com.microservice.marketing.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PromotionDTO(
        UUID id,
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
