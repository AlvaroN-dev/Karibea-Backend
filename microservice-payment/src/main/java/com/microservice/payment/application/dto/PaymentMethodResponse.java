package com.microservice.payment.application.dto;

import java.util.UUID;

/**
 * Response DTO for payment method details.
 */
public record PaymentMethodResponse(
        UUID id,
        String name,
        String type,
        String typeDisplayName,
        String providerCode,
        boolean isActive,
        String description,
        String iconUrl,
        Integer displayOrder,
        boolean requiresCardDetails,
        boolean supportsRecurring,
        boolean supportsRefund) {
}
