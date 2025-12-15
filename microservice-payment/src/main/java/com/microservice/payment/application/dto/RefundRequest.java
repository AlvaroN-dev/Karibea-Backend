package com.microservice.payment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for creating a refund.
 * Contains transaction reference and refund details.
 */
@Schema(description = "Request to create a refund for a transaction")
public class RefundRequest {

    @Schema(description = "Transaction ID to refund", 
            example = "d4e5f6a7-b8c9-0123-4567-89abcdef0123", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Transaction ID is required")
    private UUID transactionId;

    @Schema(description = "Refund amount", 
            example = "50.00", 
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0.01")
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Refund amount must be greater than zero")
    private BigDecimal amount;

    @Schema(description = "Currency code (ISO 4217)", 
            example = "USD", 
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3, 
            maxLength = 3)
    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
    private String currency;

    @Schema(description = "Reason for the refund", 
            example = "Customer requested partial refund - item damaged", 
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 500)
    @NotBlank(message = "Reason is required")
    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;

    // Constructors
    public RefundRequest() {
    }

    public RefundRequest(UUID transactionId, BigDecimal amount, String currency, String reason) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency;
        this.reason = reason;
    }

    // Getters and Setters
    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
