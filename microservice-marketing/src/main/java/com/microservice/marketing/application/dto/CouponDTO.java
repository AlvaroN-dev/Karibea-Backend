package com.microservice.marketing.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CouponDTO(
        UUID id,
        PromotionDTO promotion,
        String code,
        Integer usageLimit,
        Integer usageCount,
        Integer perUserLimit,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Boolean isActive) {
}
