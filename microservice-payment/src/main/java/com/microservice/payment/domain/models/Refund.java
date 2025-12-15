package com.microservice.payment.domain.models;

import com.microservice.payment.domain.models.enums.RefundStatus;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing a refund associated with a transaction.
 * Part of the Transaction aggregate but managed separately.
 * 
 * <p>
 * <b>Why Entity and not Value Object:</b>
 * </p>
 * <ul>
 * <li>Has unique identity (UUID)</li>
 * <li>Has its own lifecycle (pending → processing → completed/failed)</li>
 * <li>Tracked independently for audit and reconciliation</li>
 * </ul>
 * 
 * <p>
 * <b>Invariants:</b>
 * </p>
 * <ul>
 * <li>Amount must be positive</li>
 * <li>Must be associated with a valid transaction</li>
 * <li>Status transitions must be valid</li>
 * </ul>
 */
public class Refund {

    private final UUID id;
    private final UUID transactionId;
    private final Money amount;
    private final String reason;

    private RefundStatus status;
    private String providerRefundId;
    private String failureReason;
    private final LocalDateTime createdAt;
    private LocalDateTime processedAt;

    private Refund(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.transactionId = Objects.requireNonNull(builder.transactionId, "Transaction ID is required");
        this.amount = Objects.requireNonNull(builder.amount, "Amount is required");
        this.reason = builder.reason;
        this.status = builder.status != null ? builder.status : RefundStatus.PENDING;
        this.providerRefundId = builder.providerRefundId;
        this.failureReason = builder.failureReason;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.processedAt = builder.processedAt;

        validateInvariants();
    }

    private void validateInvariants() {
        if (!amount.isPositive()) {
            throw new IllegalArgumentException("Refund amount must be positive");
        }
    }

    /**
     * Factory method for creating a new refund.
     */
    public static Refund create(UUID transactionId, Money amount, String reason) {
        return Refund.builder()
                .transactionId(transactionId)
                .amount(amount)
                .reason(reason)
                .status(RefundStatus.PENDING)
                .build();
    }

    /**
     * Approves the refund request.
     */
    public void approve() {
        validateStatusTransition(RefundStatus.APPROVED);
        this.status = RefundStatus.APPROVED;
    }

    /**
     * Starts processing the refund.
     */
    public void startProcessing() {
        validateStatusTransition(RefundStatus.PROCESSING);
        this.status = RefundStatus.PROCESSING;
    }

    /**
     * Marks the refund as completed.
     */
    public void complete(String providerRefundId) {
        validateStatusTransition(RefundStatus.COMPLETED);
        this.status = RefundStatus.COMPLETED;
        this.providerRefundId = providerRefundId;
        this.processedAt = LocalDateTime.now();
    }

    /**
     * Marks the refund as rejected.
     */
    public void reject(String reason) {
        validateStatusTransition(RefundStatus.REJECTED);
        this.status = RefundStatus.REJECTED;
        this.failureReason = reason;
        this.processedAt = LocalDateTime.now();
    }

    /**
     * Marks the refund as failed.
     */
    public void fail(String reason) {
        validateStatusTransition(RefundStatus.FAILED);
        this.status = RefundStatus.FAILED;
        this.failureReason = reason;
        this.processedAt = LocalDateTime.now();
    }

    private void validateStatusTransition(RefundStatus target) {
        boolean valid = switch (this.status) {
            case PENDING -> target == RefundStatus.APPROVED || target == RefundStatus.REJECTED;
            case APPROVED -> target == RefundStatus.PROCESSING || target == RefundStatus.REJECTED;
            case PROCESSING -> target == RefundStatus.COMPLETED || target == RefundStatus.FAILED;
            case COMPLETED, REJECTED, FAILED -> false;
        };

        if (!valid) {
            throw new IllegalStateException(
                    String.format("Cannot transition refund from %s to %s", this.status, target));
        }
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public Money getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }

    public RefundStatus getStatus() {
        return status;
    }

    public String getProviderRefundId() {
        return providerRefundId;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID transactionId;
        private Money amount;
        private String reason;
        private RefundStatus status;
        private String providerRefundId;
        private String failureReason;
        private LocalDateTime createdAt;
        private LocalDateTime processedAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder transactionId(UUID transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder amount(Money amount) {
            this.amount = amount;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder status(RefundStatus status) {
            this.status = status;
            return this;
        }

        public Builder providerRefundId(String providerRefundId) {
            this.providerRefundId = providerRefundId;
            return this;
        }

        public Builder failureReason(String failureReason) {
            this.failureReason = failureReason;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder processedAt(LocalDateTime processedAt) {
            this.processedAt = processedAt;
            return this;
        }

        public Refund build() {
            return new Refund(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Refund refund = (Refund) o;
        return Objects.equals(id, refund.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Refund{id=%s, transactionId=%s, amount=%s, status=%s}",
                id, transactionId, amount, status);
    }
}
