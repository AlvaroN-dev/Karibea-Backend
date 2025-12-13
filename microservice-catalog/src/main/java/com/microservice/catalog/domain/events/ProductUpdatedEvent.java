package com.microservice.catalog.domain.events;

import java.util.UUID;

/**
 * Event raised when a product is updated.
 */
public class ProductUpdatedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "PRODUCT_UPDATED";

    private final UUID productId;
    private final String name;
    private final String sku;

    public ProductUpdatedEvent(UUID productId, String name, String sku) {
        super(EVENT_TYPE);
        this.productId = productId;
        this.name = name;
        this.sku = sku;
    }

    @Override
    public UUID getAggregateId() {
        return productId;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }
}
