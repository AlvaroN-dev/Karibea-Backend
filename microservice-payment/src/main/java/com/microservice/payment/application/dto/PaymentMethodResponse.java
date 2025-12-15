package com.microservice.payment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Response DTO for payment method details.
 * Contains system-level payment method configuration.
 */
@Schema(description = "Payment method details response")
public class PaymentMethodResponse {

    @Schema(description = "Payment method unique identifier", 
            example = "c8d9e0f1-2345-6789-abcd-ef0123456789")
    private UUID id;

    @Schema(description = "Payment method name", 
            example = "Visa Credit Card")
    private String name;

    @Schema(description = "Payment method type code", 
            example = "CREDIT_CARD",
            allowableValues = {"CREDIT_CARD", "DEBIT_CARD", "PAYPAL", "BANK_TRANSFER", "DIGITAL_WALLET"})
    private String type;

    @Schema(description = "Human-readable type name", 
            example = "Credit Card")
    private String typeDisplayName;

    @Schema(description = "Provider code", 
            example = "stripe_visa")
    private String providerCode;

    @Schema(description = "Whether this payment method is active", 
            example = "true")
    private boolean isActive;

    @Schema(description = "Payment method description", 
            example = "Pay with Visa credit card")
    private String description;

    @Schema(description = "URL to payment method icon", 
            example = "https://cdn.example.com/icons/visa.png")
    private String iconUrl;

    @Schema(description = "Display order for sorting", 
            example = "1")
    private Integer displayOrder;

    @Schema(description = "Whether card details are required", 
            example = "true")
    private boolean requiresCardDetails;

    @Schema(description = "Whether recurring payments are supported", 
            example = "true")
    private boolean supportsRecurring;

    @Schema(description = "Whether refunds are supported", 
            example = "true")
    private boolean supportsRefund;

    // Constructors
    public PaymentMethodResponse() {
    }

    public PaymentMethodResponse(UUID id, String name, String type, String typeDisplayName,
                                 String providerCode, boolean isActive, String description,
                                 String iconUrl, Integer displayOrder, boolean requiresCardDetails,
                                 boolean supportsRecurring, boolean supportsRefund) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.typeDisplayName = typeDisplayName;
        this.providerCode = providerCode;
        this.isActive = isActive;
        this.description = description;
        this.iconUrl = iconUrl;
        this.displayOrder = displayOrder;
        this.requiresCardDetails = requiresCardDetails;
        this.supportsRecurring = supportsRecurring;
        this.supportsRefund = supportsRefund;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeDisplayName() {
        return typeDisplayName;
    }

    public void setTypeDisplayName(String typeDisplayName) {
        this.typeDisplayName = typeDisplayName;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public boolean isRequiresCardDetails() {
        return requiresCardDetails;
    }

    public void setRequiresCardDetails(boolean requiresCardDetails) {
        this.requiresCardDetails = requiresCardDetails;
    }

    public boolean isSupportsRecurring() {
        return supportsRecurring;
    }

    public void setSupportsRecurring(boolean supportsRecurring) {
        this.supportsRecurring = supportsRecurring;
    }

    public boolean isSupportsRefund() {
        return supportsRefund;
    }

    public void setSupportsRefund(boolean supportsRefund) {
        this.supportsRefund = supportsRefund;
    }
}
