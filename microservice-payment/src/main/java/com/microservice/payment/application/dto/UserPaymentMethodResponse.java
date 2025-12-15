package com.microservice.payment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for user payment method details.
 * Contains user-specific saved payment method information.
 */
@Schema(description = "User saved payment method response")
public class UserPaymentMethodResponse {

    @Schema(description = "User payment method unique identifier", 
            example = "d1e2f3a4-5678-90ab-cdef-123456789012")
    private UUID id;

    @Schema(description = "Reference to system payment method", 
            example = "c8d9e0f1-2345-6789-abcd-ef0123456789")
    private UUID paymentMethodId;

    @Schema(description = "Payment method name", 
            example = "Visa Credit Card")
    private String paymentMethodName;

    @Schema(description = "User-defined alias for this payment method", 
            example = "My Personal Card")
    private String alias;

    @Schema(description = "Masked card number (last 4 digits visible)", 
            example = "************4242")
    private String maskedCardNumber;

    @Schema(description = "Card brand name", 
            example = "VISA",
            allowableValues = {"VISA", "MASTERCARD", "AMEX", "DISCOVER", "DINERS", "JCB"})
    private String cardBrand;

    @Schema(description = "Card expiry month (MM format)", 
            example = "12")
    private String expiryMonth;

    @Schema(description = "Card expiry year (YYYY format)", 
            example = "2026")
    private String expiryYear;

    @Schema(description = "Whether this is the user's default payment method", 
            example = "true")
    private boolean isDefault;

    @Schema(description = "Whether this payment method is active", 
            example = "true")
    private boolean isActive;

    @Schema(description = "Timestamp when the payment method was saved", 
            example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    // Constructors
    public UserPaymentMethodResponse() {
    }

    public UserPaymentMethodResponse(UUID id, UUID paymentMethodId, String paymentMethodName,
                                     String alias, String maskedCardNumber, String cardBrand,
                                     String expiryMonth, String expiryYear, boolean isDefault,
                                     boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.paymentMethodId = paymentMethodId;
        this.paymentMethodName = paymentMethodName;
        this.alias = alias;
        this.maskedCardNumber = maskedCardNumber;
        this.cardBrand = cardBrand;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.isDefault = isDefault;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(UUID paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public void setMaskedCardNumber(String maskedCardNumber) {
        this.maskedCardNumber = maskedCardNumber;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
