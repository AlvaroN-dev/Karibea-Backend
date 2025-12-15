package com.microservice.catalog.domain.port.in;

import com.microservice.catalog.domain.models.Product;

import java.util.UUID;

/**
 * Input port for publishing a product.
 */
public interface PublishProductUseCase {

    /**
     * Publishes a product, making it visible to customers.
     *
     * @param productId the product ID
     * @return the published product
     */
    Product execute(UUID productId);
}
