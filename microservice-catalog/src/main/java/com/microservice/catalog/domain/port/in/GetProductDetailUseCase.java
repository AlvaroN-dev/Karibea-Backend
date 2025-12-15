package com.microservice.catalog.domain.port.in;

import com.microservice.catalog.domain.models.Product;

import java.util.UUID;

/**
 * Input port for getting product detail.
 */
public interface GetProductDetailUseCase {

    /**
     * Gets the full details of a product.
     *
     * @param productId the product ID
     * @return the product with all details
     */
    Product execute(UUID productId);
}
