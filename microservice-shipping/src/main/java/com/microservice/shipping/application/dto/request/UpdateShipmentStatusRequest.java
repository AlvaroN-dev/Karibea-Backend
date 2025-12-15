package com.microservice.shipping.application.dto.request;

import com.microservice.shipping.domain.models.ShipmentStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request DTO for updating shipment status.
 * Used to transition a shipment through its lifecycle states.
 */
@Schema(description = "Request to update the status of a shipment")
public class UpdateShipmentStatusRequest {

    @Schema(
            description = "Unique identifier of the shipment",
            example = "550e8400-e29b-41d4-a716-446655440001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Shipment ID is required")
    private UUID shipmentId;

    @Schema(
            description = "New status to transition the shipment to",
            example = "IN_TRANSIT",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "New status is required")
    private ShipmentStatusEnum newStatus;

    @Schema(
            description = "Current location of the shipment",
            example = "Distribution Center, Chicago, IL",
            maxLength = 255
    )
    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    @Schema(
            description = "Reason for the status change",
            example = "Package picked up by carrier driver",
            maxLength = 500
    )
    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;

    // Constructors
    public UpdateShipmentStatusRequest() {
    }

    public UpdateShipmentStatusRequest(UUID shipmentId, ShipmentStatusEnum newStatus, 
                                        String location, String reason) {
        this.shipmentId = shipmentId;
        this.newStatus = newStatus;
        this.location = location;
        this.reason = reason;
    }

    // Getters and Setters
    public UUID getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(UUID shipmentId) {
        this.shipmentId = shipmentId;
    }

    public ShipmentStatusEnum getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(ShipmentStatusEnum newStatus) {
        this.newStatus = newStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
