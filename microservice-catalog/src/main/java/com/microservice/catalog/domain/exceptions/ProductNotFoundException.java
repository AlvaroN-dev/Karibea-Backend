package com.microservice.catalog.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a product is not found.
 */
public class ProductNotFoundException extends DomainException {

    private static final String ERROR_CODE = "PRODUCT_NOT_FOUND";

    public ProductNotFoundException(UUID productId) {
        super("Product not found with ID: " + productId, ERROR_CODE);
    }

    public ProductNotFoundException(String sku) {
        super("Product not found with SKU: " + sku, ERROR_CODE);
    }
}
