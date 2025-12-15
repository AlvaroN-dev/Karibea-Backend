package com.microservice.payment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for transaction details.
 * Contains complete transaction information including refunds.
 */
@Schema(description = "Transaction details response")
public class TransactionResponse {

    @Schema(description = "Transaction unique identifier", 
            example = "d4e5f6a7-b8c9-0123-4567-89abcdef0123")
    private UUID id;

    @Schema(description = "External order ID from Order service", 
            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID externalOrderId;

    @Schema(description = "External user ID from User service", 
            example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
    private UUID externalUserId;

    @Schema(description = "Transaction amount", 
            example = "149.99")
    private BigDecimal amount;

    @Schema(description = "Currency code", 
            example = "USD")
    private String currency;

    @Schema(description = "Current transaction status", 
            example = "COMPLETED")
    private String status;

    @Schema(description = "Human-readable status description", 
            example = "Transaction completed successfully")
    private String statusDescription;

    @Schema(description = "Transaction type", 
            example = "PAYMENT")
    private String type;

    @Schema(description = "Payment method ID used", 
            example = "c8d9e0f1-2345-6789-abcd-ef0123456789")
    private UUID paymentMethodId;

    @Schema(description = "Provider transaction ID (e.g., Stripe)", 
            example = "stripe_1702456200123")
    private String providerTransactionId;

    @Schema(description = "Failure reason if transaction failed")
    private String failureReason;

    @Schema(description = "List of refunds associated with this transaction")
    private List<RefundResponse> refunds;

    @Schema(description = "Total amount refunded", 
            example = "0.00")
    private BigDecimal totalRefunded;

    @Schema(description = "Amount available for refund", 
            example = "149.99")
    private BigDecimal refundableAmount;

    @Schema(description = "Transaction creation timestamp", 
            example = "2025-12-13T09:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", 
            example = "2025-12-13T09:31:00")
    private LocalDateTime updatedAt;

    // Constructors
    public TransactionResponse() {
    }

    public TransactionResponse(UUID id, UUID externalOrderId, UUID externalUserId, BigDecimal amount,
                               String currency, String status, String statusDescription, String type,
                               UUID paymentMethodId, String providerTransactionId, String failureReason,
                               List<RefundResponse> refunds, BigDecimal totalRefunded,
                               BigDecimal refundableAmount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.externalOrderId = externalOrderId;
        this.externalUserId = externalUserId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.statusDescription = statusDescription;
        this.type = type;
        this.paymentMethodId = paymentMethodId;
        this.providerTransactionId = providerTransactionId;
        this.failureReason = failureReason;
        this.refunds = refunds;
        this.totalRefunded = totalRefunded;
        this.refundableAmount = refundableAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(UUID paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getProviderTransactionId() {
        return providerTransactionId;
    }

    public void setProviderTransactionId(String providerTransactionId) {
        this.providerTransactionId = providerTransactionId;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public List<RefundResponse> getRefunds() {
        return refunds;
    }

    public void setRefunds(List<RefundResponse> refunds) {
        this.refunds = refunds;
    }

    public BigDecimal getTotalRefunded() {
        return totalRefunded;
    }

    public void setTotalRefunded(BigDecimal totalRefunded) {
        this.totalRefunded = totalRefunded;
    }

    public BigDecimal getRefundableAmount() {
        return refundableAmount;
    }

    public void setRefundableAmount(BigDecimal refundableAmount) {
        this.refundableAmount = refundableAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
