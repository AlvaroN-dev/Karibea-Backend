package com.microservice.order.domain.events;

import com.microservice.order.domain.models.enums.OrderStatusEnum;

import java.util.UUID;

/**
 * Event published when order status changes.
 * 
 * PURE DOMAIN - No framework dependencies.
 */
public class OrderStatusChangedEvent extends DomainEvent {

    private final OrderStatusEnum previousStatus;
    private final OrderStatusEnum newStatus;
    private final String reason;

    private OrderStatusChangedEvent(UUID orderId, OrderStatusEnum previousStatus,
            OrderStatusEnum newStatus, String reason) {
        super(orderId, "OrderStatusChanged");
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.reason = reason;
    }

    public static OrderStatusChangedEvent of(UUID orderId, OrderStatusEnum previousStatus,
            OrderStatusEnum newStatus, String reason) {
        return new OrderStatusChangedEvent(orderId, previousStatus, newStatus, reason);
    }

    // Getters
    public OrderStatusEnum getPreviousStatus() {
        return previousStatus;
    }

    public OrderStatusEnum getNewStatus() {
        return newStatus;
    }

    public String getReason() {
        return reason;
    }
}
