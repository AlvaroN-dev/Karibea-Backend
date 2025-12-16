package com.microservice.catalog.domain.models.enums;

/**
 * Enum representing the possible statuses of a Product.
 * Controls product visibility and lifecycle state.
 */
public enum ProductStatus {

    /**
     * Product is being created/edited and not visible to customers.
     */
    DRAFT,

    /**
     * Product is active and visible in the storefront.
     */
    PUBLISHED,

    /**
     * Product is temporarily disabled but can be reactivated.
     */
    INACTIVE,

    /**
     * Product is permanently archived for historical purposes.
     */
    ARCHIVED
}
