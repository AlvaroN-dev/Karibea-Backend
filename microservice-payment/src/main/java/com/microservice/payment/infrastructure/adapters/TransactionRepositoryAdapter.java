package com.microservice.payment.infrastructure.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.microservice.payment.domain.models.Transaction;
import com.microservice.payment.domain.models.enums.TransactionStatus;
import com.microservice.payment.domain.port.out.TransactionRepositoryPort;
import com.microservice.payment.infrastructure.adapters.mapper.TransactionEntityMapper;
import com.microservice.payment.infrastructure.entities.TransactionEntity;
import com.microservice.payment.infrastructure.entities.TransactionEntity.TransactionStatusEnum;
import com.microservice.payment.infrastructure.repositories.JpaTransactionRepository;

/**
 * Adapter implementing TransactionRepositoryPort.
 * Bridges domain repository contract with JPA/Spring Data implementation.
 */
@Component
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final JpaTransactionRepository jpaRepository;
    private final TransactionEntityMapper mapper;

    public TransactionRepositoryAdapter(
            JpaTransactionRepository jpaRepository,
            TransactionEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = mapper.toEntity(transaction);
        TransactionEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Transaction> findByExternalOrderId(UUID externalOrderId) {
        List<TransactionEntity> entities = jpaRepository.findByExternalOrderId(externalOrderId);
        return mapper.toDomainList(entities);
    }

    @Override
    public List<Transaction> findByExternalUserId(UUID externalUserId) {
        List<TransactionEntity> entities = jpaRepository.findByExternalUserId(externalUserId);
        return mapper.toDomainList(entities);
    }

    @Override
    public List<Transaction> findByStatus(TransactionStatus status) {
        TransactionStatusEnum statusEnum = TransactionStatusEnum.valueOf(status.name());
        List<TransactionEntity> entities = jpaRepository.findByStatus(statusEnum);
        return mapper.toDomainList(entities);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }
}
