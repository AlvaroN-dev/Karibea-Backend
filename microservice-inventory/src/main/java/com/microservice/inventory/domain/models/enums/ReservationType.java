package com.microservice.inventory.domain.models.enums;

/**
 * Reservation types for stock reservations.
 * Maps to reservations_type table.
 */
public enum ReservationType {

    CART("Cart Reservation"),
    CHECKOUT("Checkout Reservation"),
    ORDER("Order Reservation"),
    BACKORDER("Backorder Reservation"),
    PREORDER("Pre-order Reservation");

    private final String displayName;

    ReservationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
