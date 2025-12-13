package com.microservice.payment.infrastructure.adapters.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.microservice.payment.domain.models.Money;
import com.microservice.payment.domain.models.Refund;
import com.microservice.payment.domain.models.Transaction;
import com.microservice.payment.domain.models.enums.TransactionStatus;
import com.microservice.payment.domain.models.enums.TransactionType;
import com.microservice.payment.infrastructure.entities.TransactionEntity;
import com.microservice.payment.infrastructure.entities.TransactionEntity.TransactionStatusEnum;
import com.microservice.payment.infrastructure.entities.TransactionEntity.TransactionTypeEnum;

/**
 * Mapper for converting between Transaction domain model and JPA entity.
 */
@Component
public class TransactionEntityMapper {

    private final RefundEntityMapper refundMapper;

    public TransactionEntityMapper(RefundEntityMapper refundMapper) {
        this.refundMapper = refundMapper;
    }

    /**
     * Converts TransactionEntity to domain Transaction.
     */
    public Transaction toDomain(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }

        List<Refund> refunds = entity.getRefunds().stream()
                .map(refundMapper::toDomain)
                .collect(Collectors.toList());

        return Transaction.builder()
                .id(entity.getId())
                .externalOrderId(entity.getExternalOrderId())
                .externalUserId(entity.getExternalUserId())
                .amount(Money.of(entity.getAmount(), entity.getCurrency()))
                .type(mapTransactionType(entity.getType()))
                .paymentMethodId(entity.getPaymentMethodId())
                .status(mapTransactionStatus(entity.getStatus()))
                .providerTransactionId(entity.getProviderTransactionId())
                .failureReason(entity.getFailureReason())
                .refunds(refunds)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Converts domain Transaction to TransactionEntity.
     */
    public TransactionEntity toEntity(Transaction domain) {
        if (domain == null) {
            return null;
        }

        TransactionEntity entity = new TransactionEntity();
        entity.setId(domain.getId());
        entity.setExternalOrderId(domain.getExternalOrderId());
        entity.setExternalUserId(domain.getExternalUserId());
        entity.setAmount(domain.getAmount().getAmount());
        entity.setCurrency(domain.getAmount().getCurrency());
        entity.setType(mapTransactionTypeEnum(domain.getType()));
        entity.setPaymentMethodId(domain.getPaymentMethodId());
        entity.setStatus(mapTransactionStatusEnum(domain.getStatus()));
        entity.setProviderTransactionId(domain.getProviderTransactionId());
        entity.setFailureReason(domain.getFailureReason());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }

    /**
     * Converts list of TransactionEntity to list of domain Transaction.
     */
    public List<Transaction> toDomainList(List<TransactionEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private TransactionStatus mapTransactionStatus(TransactionStatusEnum status) {
        return TransactionStatus.valueOf(status.name());
    }

    private TransactionStatusEnum mapTransactionStatusEnum(TransactionStatus status) {
        return TransactionStatusEnum.valueOf(status.name());
    }

    private TransactionType mapTransactionType(TransactionTypeEnum type) {
        return TransactionType.valueOf(type.name());
    }

    private TransactionTypeEnum mapTransactionTypeEnum(TransactionType type) {
        return TransactionTypeEnum.valueOf(type.name());
    }
}
