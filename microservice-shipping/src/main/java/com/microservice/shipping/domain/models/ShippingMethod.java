package com.microservice.shipping.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ShippingMethod entity - represents a shipping method offered by a carrier.
 * PURE DOMAIN - No framework dependencies.
 */
public class ShippingMethod {

    private UUID id;
    private UUID carrierId;
    private String code;
    private String name;
    private String description;
    private Money basePrice;
    private int estimatedDaysMin;
    private int estimatedDaysMax;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private ShippingMethod() {
        this.id = UUID.randomUUID();
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static ShippingMethod create(
            UUID carrierId,
            String code,
            String name,
            String description,
            Money basePrice,
            int estimatedDaysMin,
            int estimatedDaysMax) {

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Method code cannot be blank");
        }
        if (estimatedDaysMin < 0 || estimatedDaysMax < estimatedDaysMin) {
            throw new IllegalArgumentException("Invalid estimated days range");
        }

        ShippingMethod method = new ShippingMethod();
        method.carrierId = carrierId;
        method.code = code.toUpperCase();
        method.name = name;
        method.description = description;
        method.basePrice = basePrice;
        method.estimatedDaysMin = estimatedDaysMin;
        method.estimatedDaysMax = estimatedDaysMax;

        return method;
    }

    public String getEstimatedDeliveryRange() {
        if (estimatedDaysMin == estimatedDaysMax) {
            return estimatedDaysMin + " days";
        }
        return estimatedDaysMin + "-" + estimatedDaysMax + " days";
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getCarrierId() {
        return carrierId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Money getBasePrice() {
        return basePrice;
    }

    public int getEstimatedDaysMin() {
        return estimatedDaysMin;
    }

    public int getEstimatedDaysMax() {
        return estimatedDaysMax;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Builder for reconstitution
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ShippingMethod method = new ShippingMethod();

        public Builder id(UUID id) {
            method.id = id;
            return this;
        }

        public Builder carrierId(UUID id) {
            method.carrierId = id;
            return this;
        }

        public Builder code(String code) {
            method.code = code;
            return this;
        }

        public Builder name(String name) {
            method.name = name;
            return this;
        }

        public Builder description(String desc) {
            method.description = desc;
            return this;
        }

        public Builder basePrice(Money price) {
            method.basePrice = price;
            return this;
        }

        public Builder estimatedDaysMin(int days) {
            method.estimatedDaysMin = days;
            return this;
        }

        public Builder estimatedDaysMax(int days) {
            method.estimatedDaysMax = days;
            return this;
        }

        public Builder isActive(boolean active) {
            method.isActive = active;
            return this;
        }

        public Builder createdAt(LocalDateTime dt) {
            method.createdAt = dt;
            return this;
        }

        public Builder updatedAt(LocalDateTime dt) {
            method.updatedAt = dt;
            return this;
        }

        public ShippingMethod build() {
            return method;
        }
    }
}
