package com.microservice.payment.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing a saved payment method for a user.
 * Links external user to their stored payment methods.
 * 
 * <p>
 * <b>Security considerations:</b>
 * </p>
 * <ul>
 * <li>Stores only tokenized/masked data</li>
 * <li>No raw card numbers stored</li>
 * <li>Token references external payment provider vault</li>
 * </ul>
 */
public class UserPaymentMethod {

    private final UUID id;
    private final UUID externalUserId;
    private final UUID paymentMethodId;
    private final String tokenizedData;

    private String alias;
    private String maskedCardNumber;
    private String cardBrand;
    private String expiryMonth;
    private String expiryYear;
    private boolean isDefault;
    private boolean isActive;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserPaymentMethod(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.externalUserId = Objects.requireNonNull(builder.externalUserId, "External User ID is required");
        this.paymentMethodId = Objects.requireNonNull(builder.paymentMethodId, "Payment Method ID is required");
        this.tokenizedData = Objects.requireNonNull(builder.tokenizedData, "Tokenized data is required");
        this.alias = builder.alias;
        this.maskedCardNumber = builder.maskedCardNumber;
        this.cardBrand = builder.cardBrand;
        this.expiryMonth = builder.expiryMonth;
        this.expiryYear = builder.expiryYear;
        this.isDefault = builder.isDefault;
        this.isActive = builder.isActive;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : this.createdAt;
    }

    /**
     * Factory method for creating a new user payment method.
     */
    public static UserPaymentMethod create(UUID externalUserId, UUID paymentMethodId,
            String tokenizedData, String alias) {
        return UserPaymentMethod.builder()
                .externalUserId(externalUserId)
                .paymentMethodId(paymentMethodId)
                .tokenizedData(tokenizedData)
                .alias(alias)
                .isActive(true)
                .isDefault(false)
                .build();
    }

    /**
     * Sets this as the default payment method for the user.
     */
    public void setAsDefault() {
        this.isDefault = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Removes the default flag.
     */
    public void removeDefault() {
        this.isDefault = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Deactivates this saved payment method.
     */
    public void deactivate() {
        this.isActive = false;
        this.isDefault = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the alias.
     */
    public void updateAlias(String alias) {
        this.alias = alias;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates card display information.
     */
    public void updateCardInfo(String maskedCardNumber, String cardBrand,
            String expiryMonth, String expiryYear) {
        this.maskedCardNumber = maskedCardNumber;
        this.cardBrand = cardBrand;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getExternalUserId() {
        return externalUserId;
    }

    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }

    public String getTokenizedData() {
        return tokenizedData;
    }

    public String getAlias() {
        return alias;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public boolean isDefault() {
        return isDefault;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID externalUserId;
        private UUID paymentMethodId;
        private String tokenizedData;
        private String alias;
        private String maskedCardNumber;
        private String cardBrand;
        private String expiryMonth;
        private String expiryYear;
        private boolean isDefault;
        private boolean isActive = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder externalUserId(UUID externalUserId) {
            this.externalUserId = externalUserId;
            return this;
        }

        public Builder paymentMethodId(UUID paymentMethodId) {
            this.paymentMethodId = paymentMethodId;
            return this;
        }

        public Builder tokenizedData(String tokenizedData) {
            this.tokenizedData = tokenizedData;
            return this;
        }

        public Builder alias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder maskedCardNumber(String maskedCardNumber) {
            this.maskedCardNumber = maskedCardNumber;
            return this;
        }

        public Builder cardBrand(String cardBrand) {
            this.cardBrand = cardBrand;
            return this;
        }

        public Builder expiryMonth(String expiryMonth) {
            this.expiryMonth = expiryMonth;
            return this;
        }

        public Builder expiryYear(String expiryYear) {
            this.expiryYear = expiryYear;
            return this;
        }

        public Builder isDefault(boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
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

        public UserPaymentMethod build() {
            return new UserPaymentMethod(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserPaymentMethod that = (UserPaymentMethod) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("UserPaymentMethod{id=%s, userId=%s, alias='%s', default=%s}",
                id, externalUserId, alias, isDefault);
    }
}
