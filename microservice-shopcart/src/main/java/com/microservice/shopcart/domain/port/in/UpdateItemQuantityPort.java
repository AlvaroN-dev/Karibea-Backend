package com.microservice.shopcart.domain.port.in;

import java.util.UUID;

/**
 * Input port for updating item quantity in a shopping cart.
 */
public interface UpdateItemQuantityPort {
    
    /**
     * Updates the quantity of an item in the cart.
     *
     * @param cartId The shopping cart ID
     * @param itemId The item ID
     * @param newQuantity The new quantity
     */
    void execute(UUID cartId, UUID itemId, int newQuantity);
}
