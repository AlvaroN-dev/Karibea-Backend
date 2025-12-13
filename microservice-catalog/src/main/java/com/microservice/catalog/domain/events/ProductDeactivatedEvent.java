package com.microservice.catalog.domain.events;

import java.util.UUID;

/**
 * Event raised when a product is deactivated.
 */
public class ProductDeactivatedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "PRODUCT_DEACTIVATED";

    private final UUID productId;
    private final UUID storeId;

    public ProductDeactivatedEvent(UUID productId, UUID storeId) {
        super(EVENT_TYPE);
        this.productId = productId;
        this.storeId = storeId;
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
}
