package com.microservice.shipping.domain.events;

import java.util.UUID;

/**
 * Event published when shipment is returned.
 */
public class ShipmentReturnedEvent extends DomainEvent {

    private final String reason;

    private ShipmentReturnedEvent(UUID shipmentId, String reason) {
        super(shipmentId, "ShipmentReturned");
        this.reason = reason;
    }

    public static ShipmentReturnedEvent of(UUID shipmentId, String reason) {
        return new ShipmentReturnedEvent(shipmentId, reason);
    }

    public String getReason() {
        return reason;
    }
}
