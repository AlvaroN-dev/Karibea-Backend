package com.microservice.shopcart.domain.port.in;

import java.util.UUID;

/**
 * Input port for clearing all items from a shopping cart.
 */
public interface ClearCartPort {
    
    /**
     * Clears all items from the cart.
     *
     * @param cartId The shopping cart ID
     */
    void execute(UUID cartId);
}
