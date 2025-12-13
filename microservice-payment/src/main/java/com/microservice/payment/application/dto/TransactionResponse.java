package com.microservice.payment.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for transaction details.
 * Excludes internal domain details, exposing only API-relevant data.
 */
public record TransactionResponse(
        UUID id,
        UUID externalOrderId,
        UUID externalUserId,
        BigDecimal amount,
        String currency,
        String status,
        String statusDescription,
        String type,
        UUID paymentMethodId,
        String providerTransactionId,
        String failureReason,
        List<RefundResponse> refunds,
        BigDecimal totalRefunded,
        BigDecimal refundableAmount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
