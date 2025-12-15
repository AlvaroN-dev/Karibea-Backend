package com.microservice.payment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for saving a user payment method.
 * Contains tokenized card data and user preferences.
 */
@Schema(description = "Request to save a payment method for a user")
public class UserPaymentMethodRequest {

    @Schema(description = "Payment method type ID", 
            example = "c8d9e0f1-2345-6789-abcd-ef0123456789", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Payment method ID is required")
    private UUID paymentMethodId;

    @Schema(description = "Tokenized payment data from payment provider", 
            example = "tok_visa_encrypted_abc123xyz789", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Tokenized data is required")
    private String tokenizedData;

    @Schema(description = "User-friendly alias for this payment method", 
            example = "My Personal Visa")
    private String alias;

    @Schema(description = "Masked card number (last 4 digits)", 
            example = "****4242")
    private String maskedCardNumber;

    @Schema(description = "Card brand", 
            example = "VISA",
            allowableValues = {"VISA", "MASTERCARD", "AMEX", "DISCOVER"})
    private String cardBrand;

    @Schema(description = "Card expiry month", 
            example = "12",
            minLength = 2, 
            maxLength = 2)
    private String expiryMonth;

    @Schema(description = "Card expiry year", 
            example = "2026",
            minLength = 4, 
            maxLength = 4)
    private String expiryYear;

    @Schema(description = "Set this payment method as default", 
            example = "true")
    private boolean setAsDefault;

    // Constructors
    public UserPaymentMethodRequest() {
    }

    public UserPaymentMethodRequest(UUID paymentMethodId, String tokenizedData, String alias,
                                    String maskedCardNumber, String cardBrand, String expiryMonth,
                                    String expiryYear, boolean setAsDefault) {
        this.paymentMethodId = paymentMethodId;
        this.tokenizedData = tokenizedData;
        this.alias = alias;
        this.maskedCardNumber = maskedCardNumber;
        this.cardBrand = cardBrand;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.setAsDefault = setAsDefault;
    }

    // Getters and Setters
    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(UUID paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getTokenizedData() {
        return tokenizedData;
    }

    public void setTokenizedData(String tokenizedData) {
        this.tokenizedData = tokenizedData;
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

    public boolean isSetAsDefault() {
        return setAsDefault;
    }

    public void setSetAsDefault(boolean setAsDefault) {
        this.setAsDefault = setAsDefault;
    }
}
