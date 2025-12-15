package com.microservice.payment.infrastructure.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.microservice.payment.domain.models.Refund;
import com.microservice.payment.domain.models.enums.RefundStatus;
import com.microservice.payment.domain.port.out.RefundRepositoryPort;
import com.microservice.payment.infrastructure.adapters.mapper.RefundEntityMapper;
import com.microservice.payment.infrastructure.entities.RefundEntity;
import com.microservice.payment.infrastructure.entities.RefundEntity.RefundStatusEnum;
import com.microservice.payment.infrastructure.entities.TransactionEntity;
import com.microservice.payment.infrastructure.repositories.JpaRefundRepository;
import com.microservice.payment.infrastructure.repositories.JpaTransactionRepository;

/**
 * Adapter implementing RefundRepositoryPort.
 */
@Component
public class RefundRepositoryAdapter implements RefundRepositoryPort {

    private final JpaRefundRepository jpaRepository;
    private final JpaTransactionRepository transactionRepository;
    private final RefundEntityMapper mapper;

    public RefundRepositoryAdapter(
            JpaRefundRepository jpaRepository,
            JpaTransactionRepository transactionRepository,
            RefundEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.transactionRepository = transactionRepository;
        this.mapper = mapper;
    }

    @Override
    public Refund save(Refund refund) {
        RefundEntity entity = mapper.toEntity(refund);

        // Attach transaction reference
        TransactionEntity transactionEntity = transactionRepository.findById(refund.getTransactionId())
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + refund.getTransactionId()));
        entity.setTransaction(transactionEntity);

        RefundEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Refund> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Refund> findByTransactionId(UUID transactionId) {
        List<RefundEntity> entities = jpaRepository.findByTransactionId(transactionId);
        return mapper.toDomainList(entities);
    }

    @Override
    public List<Refund> findByStatus(RefundStatus status) {
        RefundStatusEnum statusEnum = RefundStatusEnum.valueOf(status.name());
        List<RefundEntity> entities = jpaRepository.findByStatus(statusEnum);
        return mapper.toDomainList(entities);
    }
}
