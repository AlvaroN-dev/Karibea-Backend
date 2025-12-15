package com.microservice.payment.infrastructure.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microservice.payment.infrastructure.entities.UserPaymentMethodEntity;

/**
 * Spring Data JPA Repository for UserPaymentMethodEntity.
 */
@Repository
public interface JpaUserPaymentMethodRepository extends JpaRepository<UserPaymentMethodEntity, UUID> {

    List<UserPaymentMethodEntity> findByExternalUserIdAndIsActiveTrue(UUID externalUserId);

    Optional<UserPaymentMethodEntity> findByExternalUserIdAndIsDefaultTrue(UUID externalUserId);

    @Modifying
    @Query("UPDATE UserPaymentMethodEntity u SET u.isDefault = false WHERE u.externalUserId = :userId")
    void clearDefaultForUser(@Param("userId") UUID userId);

    List<UserPaymentMethodEntity> findByExternalUserIdAndIsActiveTrueOrderByIsDefaultDesc(UUID externalUserId);
}
