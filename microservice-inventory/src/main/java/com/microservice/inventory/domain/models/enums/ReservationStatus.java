package com.microservice.inventory.domain.models.enums;

/**
 * Status for stock reservations lifecycle.
 * Maps to status_stock_reservations table.
 */
public enum ReservationStatus {

    PENDING("Pending", false),
    CONFIRMED("Confirmed", false),
    EXPIRED("Expired", true),
    CANCELLED("Cancelled", true),
    COMPLETED("Completed", true);

    private final String displayName;
    private final boolean terminal;

    ReservationStatus(String displayName, boolean terminal) {
        this.displayName = displayName;
        this.terminal = terminal;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public boolean canTransitionTo(ReservationStatus target) {
        if (this.terminal)
            return false;

        return switch (this) {
            case PENDING -> target == CONFIRMED || target == EXPIRED || target == CANCELLED;
            case CONFIRMED -> target == COMPLETED || target == CANCELLED;
            default -> false;
        };
    }
}
