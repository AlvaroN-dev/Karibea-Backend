package com.microservice.payment.domain.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.microservice.payment.domain.events.DomainEvent;
import com.microservice.payment.domain.events.RefundProcessedEvent;
import com.microservice.payment.domain.events.TransactionCompletedEvent;
import com.microservice.payment.domain.events.TransactionCreatedEvent;
import com.microservice.payment.domain.events.TransactionFailedEvent;
import com.microservice.payment.domain.exceptions.InvalidTransactionStateException;
import com.microservice.payment.domain.exceptions.RefundExceedsTransactionException;
import com.microservice.payment.domain.models.enums.RefundStatus;
import com.microservice.payment.domain.models.enums.TransactionStatus;
import com.microservice.payment.domain.models.enums.TransactionType;

/**
 * Aggregate Root for Payment transactions.
 * 
 * <p>
 * This is the central entity of the Payment bounded context.
 * All invariants related to transactions are enforced here.
 * </p>
 * 
 * <p>
 * <b>Invariants:</b>
 * </p>
 * <ul>
 * <li>Amount must be positive for PAYMENT type</li>
 * <li>Status transitions must follow the state machine</li>
 * <li>Total refunds cannot exceed original transaction amount</li>
 * <li>Refunds only allowed on COMPLETED transactions</li>
 * </ul>
 * 
 * <p>
 * <b>Why Entity and not Value Object:</b>
 * </p>
 * <ul>
 * <li>Has unique identity (UUID)</li>
 * <li>Has lifecycle with state changes</li>
 * <li>Mutable within controlled boundaries</li>
 * </ul>
 */
public class Transaction {

    private final UUID id;
    private final UUID externalOrderId;
    private final UUID externalUserId;
    private final Money amount;
    private final TransactionType type;
    private final UUID paymentMethodId;

    private TransactionStatus status;
    private String providerTransactionId;
    private String failureReason;
    private final List<Refund> refunds;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Domain events to be published
    private final List<DomainEvent> domainEvents;

    private Transaction(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.externalOrderId = Objects.requireNonNull(builder.externalOrderId, "External Order ID is required");
        this.externalUserId = Objects.requireNonNull(builder.externalUserId, "External User ID is required");
        this.amount = Objects.requireNonNull(builder.amount, "Amount is required");
        this.type = Objects.requireNonNull(builder.type, "Transaction type is required");
        this.paymentMethodId = Objects.requireNonNull(builder.paymentMethodId, "Payment method is required");
        this.status = builder.status != null ? builder.status : TransactionStatus.PENDING;
        this.providerTransactionId = builder.providerTransactionId;
        this.failureReason = builder.failureReason;
        this.refunds = builder.refunds != null ? new ArrayList<>(builder.refunds) : new ArrayList<>();
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : this.createdAt;
        this.domainEvents = new ArrayList<>();

        validateInvariants();
    }

    private void validateInvariants() {
        if (type == TransactionType.PAYMENT && !amount.isPositive()) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
    }

    /**
     * Factory method for creating a new transaction.
     * Publishes TransactionCreated domain event.
     */
    public static Transaction create(UUID externalOrderId, UUID externalUserId,
            Money amount, TransactionType type,
            UUID paymentMethodId) {
        Transaction transaction = Transaction.builder()
                .externalOrderId(externalOrderId)
                .externalUserId(externalUserId)
                .amount(amount)
                .type(type)
                .paymentMethodId(paymentMethodId)
                .status(TransactionStatus.PENDING)
                .build();

        transaction.registerEvent(new TransactionCreatedEvent(
                transaction.getId(),
                transaction.getExternalOrderId(),
                transaction.getExternalUserId(),
                transaction.getAmount(),
                transaction.getType(),
                LocalDateTime.now()));

        return transaction;
    }

    /**
     * Transitions the transaction to PROCESSING status.
     */
    public void startProcessing() {
        validateTransition(TransactionStatus.PROCESSING);
        this.status = TransactionStatus.PROCESSING;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marks the transaction as completed.
     * Publishes TransactionCompleted domain event.
     */
    public void complete(String providerTransactionId) {
        validateTransition(TransactionStatus.COMPLETED);
        this.status = TransactionStatus.COMPLETED;
        this.providerTransactionId = providerTransactionId;
        this.updatedAt = LocalDateTime.now();

        registerEvent(new TransactionCompletedEvent(
                this.id,
                this.externalOrderId,
                this.externalUserId,
                this.amount,
                providerTransactionId,
                LocalDateTime.now()));
    }

    /**
     * Marks the transaction as failed.
     * Publishes TransactionFailed domain event.
     */
    public void fail(String reason) {
        validateTransition(TransactionStatus.FAILED);
        this.status = TransactionStatus.FAILED;
        this.failureReason = reason;
        this.updatedAt = LocalDateTime.now();

        registerEvent(new TransactionFailedEvent(
                this.id,
                this.externalOrderId,
                this.externalUserId,
                reason,
                LocalDateTime.now()));
    }

    /**
     * Cancels the transaction.
     */
    public void cancel() {
        validateTransition(TransactionStatus.CANCELLED);
        this.status = TransactionStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Processes a refund for this transaction.
     * Enforces invariant: total refunds <= original amount
     */
    public Refund processRefund(Money refundAmount, String reason) {
        if (!status.allowsRefund()) {
            throw new InvalidTransactionStateException(
                    "Cannot refund transaction in status: " + status);
        }

        Money totalRefunded = calculateTotalRefunded();
        Money remainingRefundable = amount.subtract(totalRefunded);

        if (refundAmount.isGreaterThan(remainingRefundable)) {
            throw new RefundExceedsTransactionException(
                    refundAmount, remainingRefundable, this.id);
        }

        Refund refund = Refund.create(this.id, refundAmount, reason);
        this.refunds.add(refund);

        // Update transaction status based on refund
        Money newTotalRefunded = totalRefunded.add(refundAmount);
        if (newTotalRefunded.equals(amount)) {
            this.status = TransactionStatus.REFUNDED;
        } else {
            this.status = TransactionStatus.PARTIALLY_REFUNDED;
        }

        this.updatedAt = LocalDateTime.now();

        registerEvent(new RefundProcessedEvent(
                refund.getId(),
                this.id,
                this.externalOrderId,
                refundAmount,
                reason,
                LocalDateTime.now()));

        return refund;
    }

    /**
     * Calculates the total amount already refunded.
     */
    public Money calculateTotalRefunded() {
        return refunds.stream()
                .filter(r -> r.getStatus().isSuccessful() || r.getStatus() == RefundStatus.PENDING
                        || r.getStatus() == RefundStatus.PROCESSING)
                .map(Refund::getAmount)
                .reduce(Money.zero(amount.getCurrency()), Money::add);
    }

    /**
     * Calculates the remaining refundable amount.
     */
    public Money calculateRefundableAmount() {
        return amount.subtract(calculateTotalRefunded());
    }

    private void validateTransition(TransactionStatus target) {
        if (!status.canTransitionTo(target)) {
            throw new InvalidTransactionStateException(
                    String.format("Cannot transition from %s to %s", status, target));
        }
    }

    private void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getExternalOrderId() {
        return externalOrderId;
    }

    public UUID getExternalUserId() {
        return externalUserId;
    }

    public Money getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public String getProviderTransactionId() {
        return providerTransactionId;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public List<Refund> getRefunds() {
        return Collections.unmodifiableList(refunds);
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
        private UUID externalOrderId;
        private UUID externalUserId;
        private Money amount;
        private TransactionType type;
        private UUID paymentMethodId;
        private TransactionStatus status;
        private String providerTransactionId;
        private String failureReason;
        private List<Refund> refunds;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder externalOrderId(UUID externalOrderId) {
            this.externalOrderId = externalOrderId;
            return this;
        }

        public Builder externalUserId(UUID externalUserId) {
            this.externalUserId = externalUserId;
            return this;
        }

        public Builder amount(Money amount) {
            this.amount = amount;
            return this;
        }

        public Builder type(TransactionType type) {
            this.type = type;
            return this;
        }

        public Builder paymentMethodId(UUID paymentMethodId) {
            this.paymentMethodId = paymentMethodId;
            return this;
        }

        public Builder status(TransactionStatus status) {
            this.status = status;
            return this;
        }

        public Builder providerTransactionId(String providerTransactionId) {
            this.providerTransactionId = providerTransactionId;
            return this;
        }

        public Builder failureReason(String failureReason) {
            this.failureReason = failureReason;
            return this;
        }

        public Builder refunds(List<Refund> refunds) {
            this.refunds = refunds;
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

        public Transaction build() {
            return new Transaction(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Transaction{id=%s, status=%s, amount=%s, type=%s}",
                id, status, amount, type);
    }
}
