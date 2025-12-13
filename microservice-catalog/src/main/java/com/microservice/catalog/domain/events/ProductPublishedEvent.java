package com.microservice.catalog.domain.events;

import java.util.UUID;

/**
 * Event raised when a product is published (made visible to customers).
 */
public class ProductPublishedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "PRODUCT_PUBLISHED";

    private final UUID productId;
    private final UUID storeId;
    private final String sku;

    public ProductPublishedEvent(UUID productId, UUID storeId, String sku) {
        super(EVENT_TYPE);
        this.productId = productId;
        this.storeId = storeId;
        this.sku = sku;
    }

    @Override
    public UUID getAggregateId() {
        return productId;
    }

    public UUID getProductId() {
        return productId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public String getSku() {
        return sku;
    }
}
