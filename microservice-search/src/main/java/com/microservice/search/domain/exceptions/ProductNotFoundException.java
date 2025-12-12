package com.microservice.search.domain.exceptions;

import java.util.UUID;

/**
 * Excepción cuando un producto no se encuentra en el índice.
 */
public class ProductNotFoundException extends DomainException {

    private final UUID productId;

    public ProductNotFoundException(UUID productId) {
        super("PRODUCT_NOT_FOUND", "Product not found: " + productId);
        this.productId = productId;
    }

    public UUID getProductId() {
        return productId;
    }
}
