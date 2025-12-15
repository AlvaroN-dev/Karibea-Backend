package com.microservice.shopcart.domain.port.in;

import java.util.UUID;

/**
 * Input port for soft deleting a shopping cart.
 */
public interface DeleteCartPort {
    
    /**
     * Soft deletes a shopping cart.
     *
     * @param cartId The cart ID to delete
     */
    void execute(UUID cartId);
}
