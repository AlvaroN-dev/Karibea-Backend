package com.microservice.payment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for creating a new transaction.
 * Contains all required information to initiate a payment transaction.
 */
@Schema(description = "Request to create a new payment transaction")
public class CreateTransactionRequest {

    @Schema(description = "External order ID from the Order service", 
            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "External order ID is required")
    private UUID externalOrderId;

    @Schema(description = "External user ID from the User service", 
            example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "External user ID is required")
    private UUID externalUserId;

    @Schema(description = "Transaction amount", 
            example = "149.99", 
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0.01")
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Schema(description = "Currency code (ISO 4217)", 
            example = "USD", 
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3, 
            maxLength = 3)
    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
    private String currency;

    @Schema(description = "Type of transaction", 
            example = "PAYMENT", 
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"PAYMENT", "AUTHORIZATION", "CAPTURE"})
    @NotBlank(message = "Transaction type is required")
    private String transactionType;

    @Schema(description = "Payment method ID to use for this transaction", 
            example = "c8d9e0f1-2345-6789-abcd-ef0123456789", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Payment method ID is required")
    private UUID paymentMethodId;

    // Constructors
    public CreateTransactionRequest() {
    }

    public CreateTransactionRequest(UUID externalOrderId, UUID externalUserId, BigDecimal amount,
                                    String currency, String transactionType, UUID paymentMethodId) {
        this.externalOrderId = externalOrderId;
        this.externalUserId = externalUserId;
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
        this.paymentMethodId = paymentMethodId;
    }

    // Getters and Setters
    public UUID getExternalOrderId() {
        return externalOrderId;
    }

    public void setExternalOrderId(UUID externalOrderId) {
        this.externalOrderId = externalOrderId;
    }

    public UUID getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(UUID externalUserId) {
        this.externalUserId = externalUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(UUID paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
}
