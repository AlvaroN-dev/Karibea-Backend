package com.microservice.catalog.domain.exceptions;

/**
 * Exception thrown when a duplicate SKU is detected.
 */
public class DuplicateSkuException extends DomainException {

    private static final String ERROR_CODE = "DUPLICATE_SKU";

    public DuplicateSkuException(String sku) {
        super("Product or variant with SKU '" + sku + "' already exists.", ERROR_CODE);
    }
}
