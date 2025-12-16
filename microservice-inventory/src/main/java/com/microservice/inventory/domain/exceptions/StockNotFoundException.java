package com.microservice.inventory.domain.exceptions;

import java.util.UUID;

/**
 * Thrown when stock is not found.
 */
public class StockNotFoundException extends DomainException {

    public StockNotFoundException(UUID stockId) {
        super(
                String.format("Stock not found with ID: %s", stockId),
                "STOCK_NOT_FOUND");
    }

    public StockNotFoundException(UUID variantId, UUID warehouseId) {
        super(
                String.format("Stock not found for variant %s in warehouse %s", variantId, warehouseId),
                "STOCK_NOT_FOUND");
    }
}
