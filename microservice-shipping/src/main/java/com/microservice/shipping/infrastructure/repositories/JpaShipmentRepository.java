package com.microservice.shipping.infrastructure.repositories;

import com.microservice.shipping.infrastructure.entities.ShipmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository for Shipment entities.
 */
@Repository
public interface JpaShipmentRepository extends JpaRepository<ShipmentEntity, UUID> {

    Optional<ShipmentEntity> findByTrackingNumber(String trackingNumber);

    @Query("SELECT s FROM ShipmentEntity s LEFT JOIN FETCH s.items LEFT JOIN FETCH s.trackingEvents WHERE s.id = :id")
    Optional<ShipmentEntity> findByIdWithDetails(@Param("id") UUID id);

    Page<ShipmentEntity> findByExternalOrderId(UUID externalOrderId, Pageable pageable);

    Page<ShipmentEntity> findByExternalCustomerId(UUID externalCustomerId, Pageable pageable);

    boolean existsByTrackingNumber(String trackingNumber);
}
