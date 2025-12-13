package com.microservice.catalog.domain.port.in;

import com.microservice.catalog.domain.models.Product;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Input port for adding a variant to a product.
 */
public interface AddVariantUseCase {

    /**
     * Command object for adding a variant.
     */
    record AddVariantCommand(
            UUID productId,
            String sku,
            String name,
            BigDecimal price,
            BigDecimal compareAtPrice,
            String barcode) {
    }

    /**
     * Adds a variant to a product.
     *
     * @param command the add variant command
     * @return the updated product
     */
    Product execute(AddVariantCommand command);
}
