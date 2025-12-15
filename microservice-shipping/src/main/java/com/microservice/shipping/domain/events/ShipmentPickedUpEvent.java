package com.microservice.shipping.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event published when shipment is picked up by carrier.
 */
public class ShipmentPickedUpEvent extends DomainEvent {

    private final LocalDateTime pickedUpAt;

    private ShipmentPickedUpEvent(UUID shipmentId, LocalDateTime pickedUpAt) {
        super(shipmentId, "ShipmentPickedUp");
        this.pickedUpAt = pickedUpAt;
    }

    public static ShipmentPickedUpEvent of(UUID shipmentId, LocalDateTime pickedUpAt) {
        return new ShipmentPickedUpEvent(shipmentId, pickedUpAt);
    }

    public LocalDateTime getPickedUpAt() {
        return pickedUpAt;
    }
}
