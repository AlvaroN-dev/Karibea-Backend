package com.microservice.shopcart.domain.port.in;

import java.util.UUID;

/**
 * Input port for adding an item to a shopping cart.
 */
public interface AddItemPort {
    
    /**
     * Adds an item to the shopping cart.
     *
     * @param cartId The shopping cart ID
     * @param productId External product ID
     * @param variantId External variant ID (optional)
     * @param storeId External store ID
     * @param quantity Quantity to add
     */
    void execute(UUID cartId, UUID productId, UUID variantId, UUID storeId, int quantity);
}
