package com.microservice.catalog.domain.port.in;

import com.microservice.catalog.domain.models.Product;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Input port for updating an existing product.
 */
public interface UpdateProductUseCase {

    /**
     * Command object containing all data to update a product.
     */
    record UpdateProductCommand(
            UUID productId,
            String name,
            String description,
            String brand,
            BigDecimal basePrice,
            BigDecimal compareAtPrice,
            String currency,
            Boolean featured) {
    }

    /**
     * Updates an existing product.
     *
     * @param command the update command
     * @return the updated product
     */
    Product execute(UpdateProductCommand command);
}
