package com.microservice.payment.infrastructure.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * JPA Entity for Transaction persistence.
 * 
 * <p>
 * <b>Why JPA entities are separate from domain:</b>
 * </p>
 * <ul>
 * <li>Domain model is free from ORM constraints</li>
 * <li>Entity can have JPA-specific annotations without polluting domain</li>
 * <li>Different lifecycle and constraints</li>
 * <li>Can evolve database schema independently</li>
 * </ul>
 */
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "external_order_id", nullable = false)
    private UUID externalOrderId;

    @Column(name = "external_user_id", nullable = false)
    private UUID externalUserId;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private TransactionStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 50)
    private TransactionTypeEnum type;

    @Column(name = "payment_method_id", nullable = false)
    private UUID paymentMethodId;

    @Column(name = "provider_transaction_id", length = 255)
    private String providerTransactionId;

    @Column(name = "failure_reason", length = 500)
    private String failureReason;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RefundEntity> refunds = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
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

    public TransactionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TransactionStatusEnum status) {
        this.status = status;
    }

    public TransactionTypeEnum getType() {
        return type;
    }

    public void setType(TransactionTypeEnum type) {
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

    public List<RefundEntity> getRefunds() {
        return refunds;
    }

    public void setRefunds(List<RefundEntity> refunds) {
        this.refunds = refunds;
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

    public void addRefund(RefundEntity refund) {
        refunds.add(refund);
        refund.setTransaction(this);
    }

    public enum TransactionStatusEnum {
        PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED, PARTIALLY_REFUNDED, REFUNDED
    }

    public enum TransactionTypeEnum {
        PAYMENT, REFUND, PARTIAL_REFUND, CHARGEBACK, AUTHORIZATION, CAPTURE, VOID
    }
}
