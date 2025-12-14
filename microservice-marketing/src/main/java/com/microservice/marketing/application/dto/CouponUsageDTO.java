package com.microservice.marketing.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponUsageDTO(
        Long id,
        Long couponId,
        String externalUserProfileId,
        String externalOrderId,
        BigDecimal discountAmount,
        LocalDateTime usedAt) {
}
