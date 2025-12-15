package com.microservice.shopcart.domain.port.in;

import java.util.UUID;

/**
 * Input port for expiring inactive shopping carts.
 */
public interface ExpireCartPort {
    
    /**
     * Expires a specific cart by ID.
     *
     * @param cartId The cart ID to expire
     */
    void execute(UUID cartId);
    
    /**
     * Expires all carts that have exceeded their expiration time.
     *
     * @return Number of carts expired
     */
    int expireInactiveCarts();
}
