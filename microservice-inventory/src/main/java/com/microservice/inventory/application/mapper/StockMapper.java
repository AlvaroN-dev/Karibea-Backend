package com.microservice.inventory.application.mapper;

import org.springframework.stereotype.Component;

import com.microservice.inventory.application.dto.StockResponse;
import com.microservice.inventory.domain.models.Stock;

/**
 * Mapper between Stock domain model and DTOs.
 */
@Component
public class StockMapper {

    public StockResponse toResponse(Stock stock) {
        return new StockResponse(
                stock.getId(),
                stock.getExternalProductId(),
                stock.getExternalVariantId(),
                stock.getWarehouseId(),
                stock.getQuantityAvailable(),
                stock.getQuantityReserved(),
                stock.getQuantityIncoming(),
                stock.getTotalQuantity(),
                stock.getLowStockThreshold(),
                stock.getReorderPoint(),
                stock.isLowStock(),
                stock.needsReorder(),
                stock.getLastRestockedAt(),
                stock.getCreatedAt(),
                stock.getUpdatedAt());
    }
}
