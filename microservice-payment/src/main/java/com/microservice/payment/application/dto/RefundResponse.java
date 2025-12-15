package com.microservice.payment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for refund details.
 * Contains complete refund information.
 */
@Schema(description = "Refund details response")
public class RefundResponse {

    @Schema(description = "Refund unique identifier", 
            example = "e5f6a7b8-c9d0-1234-5678-9abcdef01234")
    private UUID id;

    @Schema(description = "Parent transaction ID", 
            example = "d4e5f6a7-b8c9-0123-4567-89abcdef0123")
    private UUID transactionId;

    @Schema(description = "Refund amount", 
            example = "50.00")
    private BigDecimal amount;

    @Schema(description = "Currency code", 
            example = "USD")
    private String currency;

    @Schema(description = "Current refund status", 
            example = "COMPLETED",
            allowableValues = {"PENDING", "PROCESSING", "COMPLETED", "FAILED"})
    private String status;

    @Schema(description = "Human-readable status description", 
            example = "Refund completed successfully")
    private String statusDescription;

    @Schema(description = "Reason for the refund", 
            example = "Customer requested partial refund - item damaged")
    private String reason;

    @Schema(description = "Provider refund ID (e.g., Stripe)", 
            example = "refund_1702456200456")
    private String providerRefundId;

    @Schema(description = "Failure reason if refund failed")
    private String failureReason;

    @Schema(description = "Refund creation timestamp", 
            example = "2025-12-13T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Refund processing timestamp", 
            example = "2025-12-13T10:01:00")
    private LocalDateTime processedAt;

    // Constructors
    public RefundResponse() {
    }

    public RefundResponse(UUID id, UUID transactionId, BigDecimal amount, String currency,
                          String status, String statusDescription, String reason,
                          String providerRefundId, String failureReason,
                          LocalDateTime createdAt, LocalDateTime processedAt) {
        this.id = id;
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.statusDescription = statusDescription;
        this.reason = reason;
        this.providerRefundId = providerRefundId;
        this.failureReason = failureReason;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getProviderRefundId() {
        return providerRefundId;
    }

    public void setProviderRefundId(String providerRefundId) {
        this.providerRefundId = providerRefundId;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
}
