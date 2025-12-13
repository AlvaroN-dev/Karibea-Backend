package com.microservice.order.domain.events;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event published when an order is confirmed after payment.
 */
@Getter
public class OrderConfirmedEvent extends DomainEvent {

    private final UUID paymentId;
    private final LocalDateTime confirmedAt;

    private OrderConfirmedEvent(UUID orderId, UUID paymentId, LocalDateTime confirmedAt) {
        super(orderId, "OrderConfirmed");
        this.paymentId = paymentId;
        this.confirmedAt = confirmedAt;
    }

    public static OrderConfirmedEvent of(UUID orderId, UUID paymentId, LocalDateTime confirmedAt) {
        return new OrderConfirmedEvent(orderId, paymentId, confirmedAt);
    }
}
