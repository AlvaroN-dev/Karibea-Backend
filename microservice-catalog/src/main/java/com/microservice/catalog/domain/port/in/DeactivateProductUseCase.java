package com.microservice.catalog.domain.port.in;

import com.microservice.catalog.domain.models.Product;

import java.util.UUID;

/**
 * Input port for deactivating a product.
 */
public interface DeactivateProductUseCase {

    /**
     * Deactivates a product, removing it from the storefront.
     *
     * @param productId the product ID
     * @return the deactivated product
     */
    Product execute(UUID productId);
}
