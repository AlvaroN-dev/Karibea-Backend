package com.microservice.inventory.domain.port.in;

import java.util.UUID;

import com.microservice.inventory.domain.models.Stock;

/**
 * Port IN - Create stock for a variant in a warehouse.
 * SRP: Only handles stock creation.
 */
public interface CreateStockUseCase {

    Stock execute(CreateStockCommand command);

    record CreateStockCommand(
            UUID externalProductId,
            UUID externalVariantId,
            UUID warehouseId,
            int initialQuantity,
            int lowStockThreshold,
            int reorderPoint) {
        public CreateStockCommand {
            if (externalVariantId == null)
                throw new IllegalArgumentException("Variant ID is required");
            if (warehouseId == null)
                throw new IllegalArgumentException("Warehouse ID is required");
            if (initialQuantity < 0)
                throw new IllegalArgumentException("Initial quantity cannot be negative");
        }
    }
}
