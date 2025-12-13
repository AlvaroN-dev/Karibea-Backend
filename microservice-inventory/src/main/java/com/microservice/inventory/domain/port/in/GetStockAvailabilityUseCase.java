package com.microservice.inventory.domain.port.in;

import java.util.List;
import java.util.UUID;

import com.microservice.inventory.domain.models.Stock;

/**
 * Port IN - Get stock availability information.
 * SRP: Only handles stock queries.
 */
public interface GetStockAvailabilityUseCase {

    Stock getById(UUID stockId);

    Stock getByVariantAndWarehouse(UUID externalVariantId, UUID warehouseId);

    List<Stock> getByVariant(UUID externalVariantId);

    List<Stock> getByWarehouse(UUID warehouseId);

    List<Stock> getLowStockItems(UUID warehouseId);
}
