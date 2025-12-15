package com.microservice.shopcart.domain.models;

import java.util.Objects;

/**
 * Value Object representing item quantity.
 * Enforces business rules for valid quantities.
 */
public final class Quantity {
    
    private static final int MAX_QUANTITY = 999;
    private static final int MIN_QUANTITY = 1;
    
    private final int value;
    
    public Quantity(int value) {
        if (value < MIN_QUANTITY) {
            throw new IllegalArgumentException(
                String.format("Quantity must be at least %d", MIN_QUANTITY));
        }
        if (value > MAX_QUANTITY) {
            throw new IllegalArgumentException(
                String.format("Quantity cannot exceed %d", MAX_QUANTITY));
        }
        this.value = value;
    }
    
    public static Quantity of(int value) {
        return new Quantity(value);
    }
    
    public static Quantity one() {
        return new Quantity(1);
    }
    
    public Quantity add(int amount) {
        return new Quantity(this.value + amount);
    }
    
    public Quantity subtract(int amount) {
        int newValue = this.value - amount;
        if (newValue < MIN_QUANTITY) {
            throw new IllegalArgumentException(
                String.format("Resulting quantity %d is below minimum %d", newValue, MIN_QUANTITY));
        }
        return new Quantity(newValue);
    }
    
    public boolean canSubtract(int amount) {
        return (this.value - amount) >= MIN_QUANTITY;
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quantity quantity)) return false;
        return value == quantity.value;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
