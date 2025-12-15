package com.microservice.inventory.infrastructure.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.inventory.infrastructure.entities.WarehouseEntity;

@Repository
public interface JpaWarehouseRepository extends JpaRepository<WarehouseEntity, UUID> {

    Optional<WarehouseEntity> findByCode(String code);

    List<WarehouseEntity> findByIsActiveTrue();

    boolean existsByCode(String code);
}
