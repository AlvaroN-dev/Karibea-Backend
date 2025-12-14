package com.microservice.shopcart.domain.models.enums;

/**
 * Enum representing the possible states of a shopping cart.
 */
public enum CartStatus {
    
    ACTIVE("Active", "Cart is active and can receive items"),
    ABANDONED("Abandoned", "Cart was abandoned by user"),
    EXPIRED("Expired", "Cart expired due to inactivity"),
    CONVERTED("Converted", "Cart was converted to an order"),
    MERGED("Merged", "Cart was merged with another cart");
    
    private final String name;
    private final String description;
    
    CartStatus(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean canAddItems() {
        return this == ACTIVE;
    }
    
    public boolean canRemoveItems() {
        return this == ACTIVE;
    }
    
    public boolean canApplyCoupon() {
        return this == ACTIVE;
    }
    
    public boolean canConvertToOrder() {
        return this == ACTIVE;
    }
    
    public boolean isTerminal() {
        return this == EXPIRED || this == CONVERTED || this == MERGED;
    }
    
    public static CartStatus fromName(String name) {
        for (CartStatus status : values()) {
            if (status.name.equalsIgnoreCase(name)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown cart status: " + name);
    }
}
