package com.microservice.shipping.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

/**
 * Request DTO for cancelling a shipment.
 */
@Builder
public record CancelShipmentRequest(
        @NotNull(message = "Shipment ID is required") UUID shipmentId,

        @NotBlank(message = "Reason is required") String reason) {
}
