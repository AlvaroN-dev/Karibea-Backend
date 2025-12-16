package com.microservice.payment.infrastructure.adapters.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.microservice.payment.domain.models.Money;
import com.microservice.payment.domain.models.Refund;
import com.microservice.payment.domain.models.enums.RefundStatus;
import com.microservice.payment.infrastructure.entities.RefundEntity;
import com.microservice.payment.infrastructure.entities.RefundEntity.RefundStatusEnum;

/**
 * Mapper for converting between Refund domain model and JPA entity.
 */
@Component
public class RefundEntityMapper {

    /**
     * Converts RefundEntity to domain Refund.
     */
    public Refund toDomain(RefundEntity entity) {
        if (entity == null) {
            return null;
        }

        return Refund.builder()
                .id(entity.getId())
                .transactionId(entity.getTransaction().getId())
                .amount(Money.of(entity.getAmount(), entity.getCurrency()))
                .reason(entity.getReason())
                .status(mapRefundStatus(entity.getStatus()))
                .providerRefundId(entity.getProviderRefundId())
                .failureReason(entity.getFailureReason())
                .createdAt(entity.getCreatedAt())
                .processedAt(entity.getProcessedAt())
                .build();
    }

    /**
     * Converts domain Refund to RefundEntity.
     */
    public RefundEntity toEntity(Refund domain) {
        if (domain == null) {
            return null;
        }

        RefundEntity entity = new RefundEntity();
        entity.setId(domain.getId());
        entity.setAmount(domain.getAmount().getAmount());
        entity.setCurrency(domain.getAmount().getCurrency());
        entity.setReason(domain.getReason());
        entity.setStatus(mapRefundStatusEnum(domain.getStatus()));
        entity.setProviderRefundId(domain.getProviderRefundId());
        entity.setFailureReason(domain.getFailureReason());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setProcessedAt(domain.getProcessedAt());

        return entity;
    }

    /**
     * Converts list of RefundEntity to list of domain Refund.
     */
    public List<Refund> toDomainList(List<RefundEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private RefundStatus mapRefundStatus(RefundStatusEnum status) {
        return RefundStatus.valueOf(status.name());
    }

    private RefundStatusEnum mapRefundStatusEnum(RefundStatus status) {
        return RefundStatusEnum.valueOf(status.name());
    }
}
