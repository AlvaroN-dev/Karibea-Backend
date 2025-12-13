package com.microservice.shipping.application.dto.request;

import com.microservice.shipping.domain.models.TrackingEventStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Request DTO for adding a tracking event.
 */
@Builder
public record AddTrackingEventRequest(
        @NotNull(message = "Shipment ID is required") UUID shipmentId,

        @NotNull(message = "Status is required") TrackingEventStatusEnum status,

        String location,
        String description,
        LocalDateTime occurredAt) {
}
