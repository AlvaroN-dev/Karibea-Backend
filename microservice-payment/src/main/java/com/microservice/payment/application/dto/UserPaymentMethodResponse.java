package com.microservice.payment.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for user payment method details.
 */
public record UserPaymentMethodResponse(
        UUID id,
        UUID paymentMethodId,
        String paymentMethodName,
        String alias,
        String maskedCardNumber,
        String cardBrand,
        String expiryMonth,
        String expiryYear,
        boolean isDefault,
        boolean isActive,
        LocalDateTime createdAt) {
}
