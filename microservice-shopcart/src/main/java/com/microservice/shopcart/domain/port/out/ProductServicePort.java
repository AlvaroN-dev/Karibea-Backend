package com.microservice.shopcart.domain.port.out;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Output port for fetching product information from Catalog microservice.
 */
public interface ProductServicePort {
    
    /**
     * Gets product information by product and variant ID.
     *
     * @param productId The product ID
     * @param variantId The variant ID (optional)
     * @return Product information
     */
    ProductInfo getProduct(UUID productId, UUID variantId);
    
    /**
     * Product information DTO from external service.
     */
    record ProductInfo(
        UUID productId,
        UUID variantId,
        UUID storeId,
        String productName,
        String variantName,
        String sku,
        String imageUrl,
        BigDecimal price,
        String currency,
        boolean isAvailable
    ) {}
}
