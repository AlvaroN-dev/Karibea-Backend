package com.microservice.shipping.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event published when shipment is delivered.
 */
public class ShipmentDeliveredEvent extends DomainEvent {

    private final LocalDateTime deliveredAt;

    private ShipmentDeliveredEvent(UUID shipmentId, LocalDateTime deliveredAt) {
        super(shipmentId, "ShipmentDelivered");
        this.deliveredAt = deliveredAt;
    }

    public static ShipmentDeliveredEvent of(UUID shipmentId, LocalDateTime deliveredAt) {
        return new ShipmentDeliveredEvent(shipmentId, deliveredAt);
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }
}
