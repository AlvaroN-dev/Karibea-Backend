package com.microservice.order.domain.models.enums;

import java.util.Set;

/**
 * Enum representing order status with transition rules.
 */
public enum OrderStatusEnum {
    PENDING,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    COMPLETED,
    CANCELLED,
    RETURNED,
    REFUNDED;

    private static final Set<OrderStatusEnum> FINAL_STATES = Set.of(COMPLETED, CANCELLED, REFUNDED);

    /**
     * Check if transition from current status to target status is valid.
     */
    public boolean canTransitionTo(OrderStatusEnum target) {
        if (this == target) {
            return false;
        }

        return switch (this) {
            case PENDING -> target == CONFIRMED || target == CANCELLED;
            case CONFIRMED -> target == PROCESSING || target == CANCELLED;
            case PROCESSING -> target == SHIPPED || target == CANCELLED;
            case SHIPPED -> target == DELIVERED || target == RETURNED;
            case DELIVERED -> target == RETURNED || target == COMPLETED;
            case RETURNED -> target == REFUNDED;
            case COMPLETED, CANCELLED, REFUNDED -> false;
        };
    }

    public boolean isFinalState() {
        return FINAL_STATES.contains(this);
    }

    public boolean isCancellable() {
        return this == PENDING || this == CONFIRMED || this == PROCESSING;
    }
}
