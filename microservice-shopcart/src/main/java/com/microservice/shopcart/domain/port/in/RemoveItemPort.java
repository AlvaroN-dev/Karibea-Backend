package com.microservice.shopcart.domain.port.in;

import java.util.UUID;

/**
 * Input port for removing an item from a shopping cart.
 */
public interface RemoveItemPort {
    
    /**
     * Removes an item from the cart.
     *
     * @param cartId The shopping cart ID
     * @param itemId The item ID to remove
     */
    void execute(UUID cartId, UUID itemId);
}
