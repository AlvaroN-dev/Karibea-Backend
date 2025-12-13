package com.microservice.inventory.domain.exceptions;

import java.util.UUID;

/**
 * Thrown when there is not enough stock available.
 */
public class InsufficientStockException extends DomainException {

    private final UUID stockId;
    private final int requested;
    private final int available;

    public InsufficientStockException(UUID stockId, int requested, int available) {
        super(
                String.format("Insufficient stock. Requested: %d, Available: %d", requested, available),
                "INSUFFICIENT_STOCK");
        this.stockId = stockId;
        this.requested = requested;
        this.available = available;
    }

    public UUID getStockId() {
        return stockId;
    }

    public int getRequested() {
        return requested;
    }

    public int getAvailable() {
        return available;
    }
}
