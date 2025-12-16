package com.microservice.catalog.domain.events;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Event raised when a variant is added to a product.
 */
public class VariantAddedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "VARIANT_ADDED";

    private final UUID productId;
    private final UUID variantId;
    private final String sku;
    private final BigDecimal price;

    public VariantAddedEvent(UUID productId, UUID variantId, String sku, BigDecimal price) {
        super(EVENT_TYPE);
        this.productId = productId;
        this.variantId = variantId;
        this.sku = sku;
        this.price = price;
    }

    @Override
    public UUID getAggregateId() {
        return productId;
    }

    public UUID getProductId() {
        return productId;
    }

    public UUID getVariantId() {
        return variantId;
    }

    public String getSku() {
        return sku;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
