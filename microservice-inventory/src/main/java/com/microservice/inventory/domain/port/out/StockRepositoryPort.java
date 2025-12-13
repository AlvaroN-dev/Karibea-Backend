package com.microservice.inventory.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.microservice.inventory.domain.models.Stock;

/**
 * Port OUT - Stock repository contract.
 */
public interface StockRepositoryPort {

    Stock save(Stock stock);

    Optional<Stock> findById(UUID stockId);

    Optional<Stock> findByVariantAndWarehouse(UUID externalVariantId, UUID warehouseId);

    List<Stock> findByExternalVariantId(UUID externalVariantId);

    List<Stock> findByWarehouseId(UUID warehouseId);

    List<Stock> findLowStockByWarehouse(UUID warehouseId);

    boolean existsByVariantAndWarehouse(UUID externalVariantId, UUID warehouseId);
}
