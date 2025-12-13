package com.microservice.shipping.application.dto.request;

import com.microservice.shipping.domain.models.ShipmentStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

/**
 * Request DTO for updating shipment status.
 */
@Builder
public record UpdateShipmentStatusRequest(
        @NotNull(message = "Shipment ID is required") UUID shipmentId,

        @NotNull(message = "New status is required") ShipmentStatusEnum newStatus,

        String location,
        String reason) {
}
