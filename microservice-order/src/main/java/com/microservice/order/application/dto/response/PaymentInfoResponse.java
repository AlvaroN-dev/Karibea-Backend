package com.microservice.order.application.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Payment information.
 * Contains enriched data from external payment microservice context.
 */
@Builder
public record PaymentInfoResponse(
        UUID id,
        String transactionId,
        String paymentMethod,
        String paymentStatus,
        BigDecimal amount,
        String currency,
        LocalDateTime processedAt
) {
    /**
     * Creates a PaymentInfoResponse with only the ID (when external data is not available).
     */
    public static PaymentInfoResponse withIdOnly(UUID id) {
        return PaymentInfoResponse.builder()
                .id(id)
                .build();
    }
}
