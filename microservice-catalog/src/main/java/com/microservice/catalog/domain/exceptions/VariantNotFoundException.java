package com.microservice.catalog.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a variant is not found.
 */
public class VariantNotFoundException extends DomainException {

    private static final String ERROR_CODE = "VARIANT_NOT_FOUND";

    public VariantNotFoundException(UUID variantId) {
        super("Variant not found with ID: " + variantId, ERROR_CODE);
    }

    public VariantNotFoundException(String sku) {
        super("Variant not found with SKU: " + sku, ERROR_CODE);
    }
}
