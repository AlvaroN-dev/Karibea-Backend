package com.microservice.payment.infrastructure.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.payment.infrastructure.entities.TransactionEntity;
import com.microservice.payment.infrastructure.entities.TransactionEntity.TransactionStatusEnum;

/**
 * Spring Data JPA Repository for TransactionEntity.
 */
@Repository
public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    List<TransactionEntity> findByExternalOrderId(UUID externalOrderId);

    List<TransactionEntity> findByExternalUserId(UUID externalUserId);

    List<TransactionEntity> findByStatus(TransactionStatusEnum status);

    List<TransactionEntity> findByExternalUserIdOrderByCreatedAtDesc(UUID externalUserId);
}
