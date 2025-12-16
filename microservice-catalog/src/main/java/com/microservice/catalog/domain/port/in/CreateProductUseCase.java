package com.microservice.catalog.domain.port.in;

import com.microservice.catalog.domain.models.Product;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Input port for creating a new product.
 * Follows the Interface Segregation Principle (ISP) - small, focused
 * interfaces.
 */
public interface CreateProductUseCase {

    /**
     * Command object containing all data needed to create a product.
     */
    record CreateProductCommand(
            UUID storeId,
            String sku,
            String name,
            String description,
            String brand,
            BigDecimal basePrice,
            BigDecimal compareAtPrice,
            String currency) {
    }

    /**
     * Creates a new product.
     *
     * @param command the creation command
     * @return the created product
     */
    Product execute(CreateProductCommand command);
}
