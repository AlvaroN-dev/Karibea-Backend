package com.microservice.inventory.domain.exceptions;

import java.util.UUID;

/**
 * Thrown when warehouse is not found.
 */
public class WarehouseNotFoundException extends DomainException {

    public WarehouseNotFoundException(UUID warehouseId) {
        super(
                String.format("Warehouse not found with ID: %s", warehouseId),
                "WAREHOUSE_NOT_FOUND");
    }
}
