package com.microservice.shipping.domain.events;

import com.microservice.shipping.domain.models.ShipmentStatusEnum;

import java.util.UUID;

/**
 * Event published when shipment status changes.
 */
public class ShipmentStatusChangedEvent extends DomainEvent {

    private final ShipmentStatusEnum previousStatus;
    private final ShipmentStatusEnum newStatus;
    private final String reason;

    private ShipmentStatusChangedEvent(UUID shipmentId, ShipmentStatusEnum previousStatus,
            ShipmentStatusEnum newStatus, String reason) {
        super(shipmentId, "ShipmentStatusChanged");
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.reason = reason;
    }

    public static ShipmentStatusChangedEvent of(UUID shipmentId, ShipmentStatusEnum previousStatus,
            ShipmentStatusEnum newStatus, String reason) {
        return new ShipmentStatusChangedEvent(shipmentId, previousStatus, newStatus, reason);
    }

    public ShipmentStatusEnum getPreviousStatus() {
        return previousStatus;
    }

    public ShipmentStatusEnum getNewStatus() {
        return newStatus;
    }

    public String getReason() {
        return reason;
    }
}
