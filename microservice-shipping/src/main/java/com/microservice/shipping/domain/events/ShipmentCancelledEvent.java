package com.microservice.shipping.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event published when shipment is cancelled.
 */
public class ShipmentCancelledEvent extends DomainEvent {

    private final String reason;
    private final LocalDateTime cancelledAt;

    private ShipmentCancelledEvent(UUID shipmentId, String reason, LocalDateTime cancelledAt) {
        super(shipmentId, "ShipmentCancelled");
        this.reason = reason;
        this.cancelledAt = cancelledAt;
    }

    public static ShipmentCancelledEvent of(UUID shipmentId, String reason, LocalDateTime cancelledAt) {
        return new ShipmentCancelledEvent(shipmentId, reason, cancelledAt);
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }
}
