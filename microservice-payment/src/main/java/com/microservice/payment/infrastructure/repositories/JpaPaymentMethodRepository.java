package com.microservice.payment.infrastructure.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.payment.infrastructure.entities.PaymentMethodEntity;
import com.microservice.payment.infrastructure.entities.PaymentMethodEntity.PaymentMethodTypeEnum;

/**
 * Spring Data JPA Repository for PaymentMethodEntity.
 */
@Repository
public interface JpaPaymentMethodRepository extends JpaRepository<PaymentMethodEntity, UUID> {

    List<PaymentMethodEntity> findByIsActiveTrue();

    List<PaymentMethodEntity> findByType(PaymentMethodTypeEnum type);

    Optional<PaymentMethodEntity> findByProviderCode(String providerCode);

    List<PaymentMethodEntity> findByIsActiveTrueOrderByDisplayOrderAsc();
}
