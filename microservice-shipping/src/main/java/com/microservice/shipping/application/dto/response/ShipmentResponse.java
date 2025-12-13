package com.microservice.shipping.application.dto.response;

import com.microservice.shipping.domain.models.ShipmentStatusEnum;
import com.microservice.shipping.domain.models.TrackingEventStatusEnum;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for Shipment.
 */
@Builder
public record ShipmentResponse(
        UUID id,
        String trackingNumber,
        UUID orderId,
        UUID storeId,
        UUID customerId,
        UUID carrierId,
        String carrierCode,
        String carrierName,
        String shippingMethodName,
        ShipmentStatusEnum status,
        AddressResponse originAddress,
        AddressResponse destinationAddress,
        BigDecimal shippingCost,
        Double weightKg,
        String dimensions,
        String notes,
        LocalDateTime estimatedDeliveryDate,
        LocalDateTime pickedUpAt,
        LocalDateTime deliveredAt,
        LocalDateTime cancelledAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ShipmentItemResponse> items,
        List<TrackingEventResponse> trackingEvents) {
    @Builder
    public record AddressResponse(
            String street,
            String city,
            String state,
            String zipCode,
            String country,
            String fullAddress) {
    }

    @Builder
    public record ShipmentItemResponse(
            UUID id,
            UUID productId,
            String productName,
            String sku,
            int quantity) {
    }

    @Builder
    public record TrackingEventResponse(
            UUID id,
            TrackingEventStatusEnum status,
            String location,
            String description,
            LocalDateTime occurredAt,
            LocalDateTime createdAt) {
    }
}
