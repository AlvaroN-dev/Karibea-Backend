package com.microservice.catalog.domain.events;

import java.util.UUID;

/**
 * Event raised when a new product is created.
 */
public class ProductCreatedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "PRODUCT_CREATED";

    private final UUID productId;
    private final UUID storeId;
    private final String sku;
    private final String name;

    public ProductCreatedEvent(UUID productId, UUID storeId, String sku, String name) {
        super(EVENT_TYPE);
        this.productId = productId;
        this.storeId = storeId;
        this.sku = sku;
        this.name = name;
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

    public String getName() {
        return name;
    }
}
