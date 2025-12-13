package com.microservice.payment.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for refund details.
 */
public record RefundResponse(
        UUID id,
        UUID transactionId,
        BigDecimal amount,
        String currency,
        String status,
        String statusDescription,
        String reason,
        String providerRefundId,
        String failureReason,
        LocalDateTime createdAt,
        LocalDateTime processedAt) {
}
