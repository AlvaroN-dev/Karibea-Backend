package com.microservice.order.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * OrderStatusHistory entity - audit trail for order status changes.
 */
@Getter
@Builder
@AllArgsConstructor
public class OrderStatusHistory {

    private UUID id;
    private UUID orderId;
    private OrderStatusEnum previousStatus;
    private OrderStatusEnum newStatus;
    private String changedBy;
    private String reason;
    private String metadata;
    private LocalDateTime createdAt;

    public OrderStatusHistory() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Factory method to create a new status history entry.
     */
    public static OrderStatusHistory create(
            UUID orderId,
            OrderStatusEnum previousStatus,
            OrderStatusEnum newStatus,
            String changedBy,
            String reason) {

        OrderStatusHistory history = new OrderStatusHistory();
        history.orderId = orderId;
        history.previousStatus = previousStatus;
        history.newStatus = newStatus;
        history.changedBy = changedBy;
        history.reason = reason;

        return history;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
