package com.microservice.shopcart.domain.port.out;

import java.util.UUID;

/**
 * Output port for fetching store information from Store microservice.
 */
public interface StoreServicePort {
    
    /**
     * Gets store information by store ID.
     *
     * @param storeId The store ID
     * @return Store information
     */
    StoreInfo getStore(UUID storeId);
    
    /**
     * Store information DTO from external service.
     */
    record StoreInfo(
        UUID storeId,
        String name,
        String logoUrl,
        String email
    ) {}
}
