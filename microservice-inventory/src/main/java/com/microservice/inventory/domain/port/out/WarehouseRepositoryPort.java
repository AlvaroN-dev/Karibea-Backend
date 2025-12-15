package com.microservice.inventory.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.microservice.inventory.domain.models.Warehouse;

/**
 * Port OUT - Warehouse repository contract.
 */
public interface WarehouseRepositoryPort {

    Warehouse save(Warehouse warehouse);

    Optional<Warehouse> findById(UUID warehouseId);

    Optional<Warehouse> findByCode(String code);

    List<Warehouse> findAll();

    List<Warehouse> findAllActive();

    boolean existsByCode(String code);
}
