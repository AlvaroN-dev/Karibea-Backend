package com.microservice.shipping.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request DTO for cancelling a shipment.
 * Contains the shipment identifier and cancellation reason.
 */
@Schema(description = "Request to cancel a shipment")
public class CancelShipmentRequest {

    @Schema(
            description = "Unique identifier of the shipment to cancel",
            example = "550e8400-e29b-41d4-a716-446655440001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Shipment ID is required")
    private UUID shipmentId;

    @Schema(
            description = "Reason for cancelling the shipment",
            example = "Customer requested cancellation before dispatch",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 1,
            maxLength = 500
    )
    @NotBlank(message = "Reason is required")
    @Size(min = 1, max = 500, message = "Reason must be between 1 and 500 characters")
    private String reason;

    // Constructors
    public CancelShipmentRequest() {
    }

    public CancelShipmentRequest(UUID shipmentId, String reason) {
        this.shipmentId = shipmentId;
        this.reason = reason;
    }

    // Getters and Setters
    public UUID getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(UUID shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
