package com.microservice.order.application.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Shipment information.
 * Contains enriched data from external shipping microservice context.
 */
@Builder
public record ShipmentInfoResponse(
        UUID id,
        String trackingNumber,
        String carrierName,
        String carrierCode,
        String shippingMethod,
        String status,
        LocalDate estimatedDeliveryDate,
        LocalDateTime shippedAt,
        LocalDateTime deliveredAt
) {
    /**
     * Creates a ShipmentInfoResponse with only the ID (when external data is not available).
     */
    public static ShipmentInfoResponse withIdOnly(UUID id) {
        return ShipmentInfoResponse.builder()
                .id(id)
                .build();
    }
}
