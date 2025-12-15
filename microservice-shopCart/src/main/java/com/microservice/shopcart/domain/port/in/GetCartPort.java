package com.microservice.shopcart.domain.port.in;

import com.microservice.shopcart.domain.models.ShoppingCart;
import java.util.Optional;
import java.util.UUID;

/**
 * Input port for getting shopping cart details.
 */
public interface GetCartPort {
    
    /**
     * Gets a shopping cart by ID.
     *
     * @param cartId The shopping cart ID
     * @return The shopping cart if found
     */
    Optional<ShoppingCart> execute(UUID cartId);
    
    /**
     * Gets a shopping cart by user profile ID.
     *
     * @param userProfileId The external user profile ID
     * @return The active shopping cart if found
     */
    Optional<ShoppingCart> findByUserProfileId(UUID userProfileId);
    
    /**
     * Gets a shopping cart by session ID.
     *
     * @param sessionId The session ID
     * @return The active shopping cart if found
     */
    Optional<ShoppingCart> findBySessionId(String sessionId);
}
