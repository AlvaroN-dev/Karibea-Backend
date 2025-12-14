package com.microservice.marketing.application.dto;

import java.time.LocalDateTime;

public record CouponDTO(
        Long id,
        Long promotionId,
        String code,
        Integer usageLimit,
        Integer usageCount,
        Integer perUserLimit,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Boolean isActive) {
}
