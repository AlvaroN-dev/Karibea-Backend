package com.microservice.shipping.domain.models;

import java.util.Set;

/**
 * Enum representing shipment status with transition rules.
 * PURE DOMAIN - No framework dependencies.
 */
public enum ShipmentStatusEnum {
    PENDING,
    CONFIRMED,
    PICKED_UP,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    FAILED_DELIVERY,
    RETURNED,
    CANCELLED;

    private static final Set<ShipmentStatusEnum> FINAL_STATES = Set.of(DELIVERED, RETURNED, CANCELLED);

    /**
     * Check if transition from current status to target status is valid.
     */
    public boolean canTransitionTo(ShipmentStatusEnum target) {
        if (this == target) {
            return false;
        }

        return switch (this) {
            case PENDING -> target == CONFIRMED || target == CANCELLED;
            case CONFIRMED -> target == PICKED_UP || target == CANCELLED;
            case PICKED_UP -> target == IN_TRANSIT || target == CANCELLED;
            case IN_TRANSIT -> target == OUT_FOR_DELIVERY || target == RETURNED;
            case OUT_FOR_DELIVERY -> target == DELIVERED || target == FAILED_DELIVERY;
            case FAILED_DELIVERY -> target == OUT_FOR_DELIVERY || target == RETURNED;
            case DELIVERED, RETURNED, CANCELLED -> false;
        };
    }

    public boolean isFinalState() {
        return FINAL_STATES.contains(this);
    }

    public boolean isCancellable() {
        return this == PENDING || this == CONFIRMED || this == PICKED_UP;
    }
}
