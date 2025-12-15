package com.microservice.payment.infrastructure.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.payment.infrastructure.entities.RefundEntity;
import com.microservice.payment.infrastructure.entities.RefundEntity.RefundStatusEnum;

/**
 * Spring Data JPA Repository for RefundEntity.
 */
@Repository
public interface JpaRefundRepository extends JpaRepository<RefundEntity, UUID> {

    List<RefundEntity> findByTransactionId(UUID transactionId);

    List<RefundEntity> findByStatus(RefundStatusEnum status);
}
