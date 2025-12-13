package com.microservice.order.domain.events;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event published when an order is cancelled.
 */
@Getter
public class OrderCancelledEvent extends DomainEvent {

    private final String reason;
    private final LocalDateTime cancelledAt;

    private OrderCancelledEvent(UUID orderId, String reason, LocalDateTime cancelledAt) {
        super(orderId, "OrderCancelled");
        this.reason = reason;
        this.cancelledAt = cancelledAt;
    }

    public static OrderCancelledEvent of(UUID orderId, String reason, LocalDateTime cancelledAt) {
        return new OrderCancelledEvent(orderId, reason, cancelledAt);
    }
}
