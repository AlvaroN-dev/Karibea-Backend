package com.microservice.inventory.domain.models.enums;

/**
 * Movement types for stock adjustments.
 * Maps to movement_type table.
 */
public enum MovementType {

    PURCHASE("Purchase", true),
    SALE("Sale", false),
    RETURN("Return", true),
    ADJUSTMENT_IN("Adjustment In", true),
    ADJUSTMENT_OUT("Adjustment Out", false),
    TRANSFER_IN("Transfer In", true),
    TRANSFER_OUT("Transfer Out", false),
    RESERVATION("Reservation", false),
    RESERVATION_RELEASE("Reservation Release", true),
    DAMAGED("Damaged", false),
    EXPIRED("Expired", false);

    private final String displayName;
    private final boolean increasesStock;

    MovementType(String displayName, boolean increasesStock) {
        this.displayName = displayName;
        this.increasesStock = increasesStock;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean increasesStock() {
        return increasesStock;
    }

    public boolean decreasesStock() {
        return !increasesStock;
    }
}
