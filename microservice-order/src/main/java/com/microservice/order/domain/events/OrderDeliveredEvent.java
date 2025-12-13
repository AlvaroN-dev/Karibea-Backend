package com.microservice.order.domain.events;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event published when an order is delivered.
 */
@Getter
public class OrderDeliveredEvent extends DomainEvent {

    private final LocalDateTime deliveredAt;

    private OrderDeliveredEvent(UUID orderId, LocalDateTime deliveredAt) {
        super(orderId, "OrderDelivered");
        this.deliveredAt = deliveredAt;
    }

    public static OrderDeliveredEvent of(UUID orderId, LocalDateTime deliveredAt) {
        return new OrderDeliveredEvent(orderId, deliveredAt);
    }
}
