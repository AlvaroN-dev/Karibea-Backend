package com.microservice.shopcart.domain.port.out;

import com.microservice.shopcart.domain.models.ShoppingCart;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for shopping cart persistence operations.
 */
public interface CartRepositoryPort {
    
    /**
     * Saves a shopping cart.
     *
     * @param cart The shopping cart to save
     * @return The saved shopping cart
     */
    ShoppingCart save(ShoppingCart cart);
    
    /**
     * Finds a shopping cart by ID.
     *
     * @param id The cart ID
     * @return The shopping cart if found
     */
    Optional<ShoppingCart> findById(UUID id);
    
    /**
     * Finds an active shopping cart by user profile ID.
     *
     * @param userProfileId The external user profile ID
     * @return The active shopping cart if found
     */
    Optional<ShoppingCart> findActiveByUserProfileId(UUID userProfileId);
    
    /**
     * Finds an active shopping cart by session ID.
     *
     * @param sessionId The session ID
     * @return The active shopping cart if found
     */
    Optional<ShoppingCart> findActiveBySessionId(String sessionId);
    
    /**
     * Finds all expired carts that need to be processed.
     *
     * @return List of expired shopping carts
     */
    List<ShoppingCart> findExpiredCarts();
    
    /**
     * Soft deletes a shopping cart.
     *
     * @param id The cart ID
     */
    void softDelete(UUID id);
    
    /**
     * Checks if a cart exists by ID.
     *
     * @param id The cart ID
     * @return true if exists
     */
    boolean existsById(UUID id);
}
