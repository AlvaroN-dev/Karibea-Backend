package com.microservice.order.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event published when an order is shipped.
 * 
 * PURE DOMAIN - No framework dependencies.
 */
public class OrderShippedEvent extends DomainEvent {

    private final UUID shipmentId;
    private final LocalDateTime shippedAt;

    private OrderShippedEvent(UUID orderId, UUID shipmentId, LocalDateTime shippedAt) {
        super(orderId, "OrderShipped");
        this.shipmentId = shipmentId;
        this.shippedAt = shippedAt;
    }

    public static OrderShippedEvent of(UUID orderId, UUID shipmentId, LocalDateTime shippedAt) {
        return new OrderShippedEvent(orderId, shipmentId, shippedAt);
    }

    // Getters
    public UUID getShipmentId() {
        return shipmentId;
    }

    public LocalDateTime getShippedAt() {
        return shippedAt;
    }
}
