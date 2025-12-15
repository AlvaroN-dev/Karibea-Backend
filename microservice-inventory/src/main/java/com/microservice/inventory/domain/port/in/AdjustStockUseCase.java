package com.microservice.inventory.domain.port.in;

import java.util.UUID;

import com.microservice.inventory.domain.models.Stock;
import com.microservice.inventory.domain.models.enums.MovementType;

/**
 * Port IN - Adjust stock quantity (increase/decrease).
 * SRP: Only handles stock adjustments.
 */
public interface AdjustStockUseCase {

    Stock execute(AdjustStockCommand command);

    record AdjustStockCommand(
            UUID stockId,
            MovementType movementType,
            int quantity,
            String referenceType,
            UUID externalReferenceId,
            UUID performedById,
            String note) {
        public AdjustStockCommand {
            if (stockId == null)
                throw new IllegalArgumentException("Stock ID is required");
            if (movementType == null)
                throw new IllegalArgumentException("Movement type is required");
            if (quantity <= 0)
                throw new IllegalArgumentException("Quantity must be positive");
        }
    }
}
