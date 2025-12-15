package com.microservice.marketing.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CouponUsageDTO(
        UUID id,
        UUID couponId,
        String externalUserProfileId,
        String externalOrderId,
        BigDecimal discountAmount,
        LocalDateTime usedAt) {
}
