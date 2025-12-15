package com.microservice.payment.domain.models;

import com.microservice.payment.domain.models.enums.PaymentMethodType;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing an available payment method in the system.
 * This is a catalog entity - defines what payment methods are available.
 * 
 * <p>
 * <b>Why Entity:</b>
 * </p>
 * <ul>
 * <li>Has unique identity</li>
 * <li>Can be enabled/disabled over time</li>
 * <li>Configuration changes independently</li>
 * </ul>
 */
public class PaymentMethod {

    private final UUID id;
    private final String name;
    private final PaymentMethodType type;
    private final String providerCode;

    private boolean isActive;
    private String description;
    private String iconUrl;
    private Integer displayOrder;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private PaymentMethod(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.name = Objects.requireNonNull(builder.name, "Name is required");
        this.type = Objects.requireNonNull(builder.type, "Type is required");
        this.providerCode = Objects.requireNonNull(builder.providerCode, "Provider code is required");
        this.isActive = builder.isActive;
        this.description = builder.description;
        this.iconUrl = builder.iconUrl;
        this.displayOrder = builder.displayOrder != null ? builder.displayOrder : 0;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : this.createdAt;
    }

    /**
     * Factory method for creating a new payment method.
     */
    public static PaymentMethod create(String name, PaymentMethodType type, String providerCode) {
        return PaymentMethod.builder()
                .name(name)
                .type(type)
                .providerCode(providerCode)
                .isActive(true)
                .build();
    }

    /**
     * Activates this payment method.
     */
    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Deactivates this payment method.
     */
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the display configuration.
     */
    public void updateDisplay(String description, String iconUrl, Integer displayOrder) {
        this.description = description;
        this.iconUrl = iconUrl;
        this.displayOrder = displayOrder != null ? displayOrder : this.displayOrder;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PaymentMethodType getType() {
        return type;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private PaymentMethodType type;
        private String providerCode;
        private boolean isActive = true;
        private String description;
        private String iconUrl;
        private Integer displayOrder;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(PaymentMethodType type) {
            this.type = type;
            return this;
        }

        public Builder providerCode(String providerCode) {
            this.providerCode = providerCode;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder iconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
            return this;
        }

        public Builder displayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PaymentMethod build() {
            return new PaymentMethod(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PaymentMethod that = (PaymentMethod) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("PaymentMethod{id=%s, name='%s', type=%s, active=%s}",
                id, name, type, isActive);
    }
}
