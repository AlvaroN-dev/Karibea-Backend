package com.microservice.order.domain.models;

import com.microservice.order.domain.models.enums.OrderStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * OrderStatusHistory entity - audit trail for order status changes.
 * 
 * PURE DOMAIN - No framework dependencies.
 */
public class OrderStatusHistory {

    private UUID id;
    private UUID orderId;
    private OrderStatusEnum previousStatus;
    private OrderStatusEnum newStatus;
    private String changedBy;
    private String reason;
    private String metadata;
    private LocalDateTime createdAt;

    /**
     * Private constructor - use factory method.
     */
    private OrderStatusHistory() {
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

    // ========== Getters (Pure Java) ==========

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public OrderStatusEnum getPreviousStatus() {
        return previousStatus;
    }

    public OrderStatusEnum getNewStatus() {
        return newStatus;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public String getReason() {
        return reason;
    }

    public String getMetadata() {
        return metadata;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // ========== Builder for Reconstitution ==========

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final OrderStatusHistory history = new OrderStatusHistory();

        public Builder id(UUID id) {
            history.id = id;
            return this;
        }

        public Builder orderId(UUID orderId) {
            history.orderId = orderId;
            return this;
        }

        public Builder previousStatus(OrderStatusEnum status) {
            history.previousStatus = status;
            return this;
        }

        public Builder newStatus(OrderStatusEnum status) {
            history.newStatus = status;
            return this;
        }

        public Builder changedBy(String by) {
            history.changedBy = by;
            return this;
        }

        public Builder reason(String reason) {
            history.reason = reason;
            return this;
        }

        public Builder metadata(String meta) {
            history.metadata = meta;
            return this;
        }

        public Builder createdAt(LocalDateTime dt) {
            history.createdAt = dt;
            return this;
        }

        public OrderStatusHistory build() {
            return history;
        }
    }
}
