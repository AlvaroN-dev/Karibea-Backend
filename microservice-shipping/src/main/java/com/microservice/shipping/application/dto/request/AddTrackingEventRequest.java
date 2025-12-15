package com.microservice.shipping.application.dto.request;

import com.microservice.shipping.domain.models.TrackingEventStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Request DTO for adding a tracking event to a shipment.
 * Tracking events provide real-time updates on shipment progress.
 */
@Schema(description = "Request to add a tracking event to a shipment")
public class AddTrackingEventRequest {

    @Schema(
            description = "Unique identifier of the shipment",
            example = "550e8400-e29b-41d4-a716-446655440001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Shipment ID is required")
    private UUID shipmentId;

    @Schema(
            description = "Status of the tracking event",
            example = "IN_TRANSIT",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Status is required")
    private TrackingEventStatusEnum status;

    @Schema(
            description = "Location where the event occurred",
            example = "Distribution Center, Miami, FL",
            maxLength = 255
    )
    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    @Schema(
            description = "Description of the tracking event",
            example = "Package arrived at distribution center and is being processed",
            maxLength = 1000
    )
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Schema(
            description = "Timestamp when the event occurred (defaults to current time if not provided)",
            example = "2024-12-15T10:30:00"
    )
    private LocalDateTime occurredAt;

    // Constructors
    public AddTrackingEventRequest() {
    }

    public AddTrackingEventRequest(UUID shipmentId, TrackingEventStatusEnum status, 
                                    String location, String description, LocalDateTime occurredAt) {
        this.shipmentId = shipmentId;
        this.status = status;
        this.location = location;
        this.description = description;
        this.occurredAt = occurredAt;
    }

    // Getters and Setters
    public UUID getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(UUID shipmentId) {
        this.shipmentId = shipmentId;
    }

    public TrackingEventStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TrackingEventStatusEnum status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
}
