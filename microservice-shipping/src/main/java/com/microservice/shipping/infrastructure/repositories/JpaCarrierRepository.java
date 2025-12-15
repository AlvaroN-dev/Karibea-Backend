package com.microservice.shipping.infrastructure.repositories;

import com.microservice.shipping.infrastructure.entities.CarrierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository for Carrier entities.
 */
@Repository
public interface JpaCarrierRepository extends JpaRepository<CarrierEntity, UUID> {

    Optional<CarrierEntity> findByCode(String code);

    List<CarrierEntity> findByIsActiveTrue();

    boolean existsByCode(String code);
}
