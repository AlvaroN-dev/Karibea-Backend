package com.microservice.shopcart.domain.port.in;

import com.microservice.shopcart.domain.models.ShoppingCart;
import java.util.UUID;

/**
 * Input port for creating a new shopping cart.
 */
public interface CreateCartPort {
    
    /**
     * Creates a new shopping cart.
     *
     * @param userProfileId External user profile ID (can be null for guest)
     * @param sessionId Session ID for tracking
     * @param currency Cart currency (e.g., "USD", "EUR")
     * @return The created shopping cart
     */
    ShoppingCart execute(UUID userProfileId, String sessionId, String currency);
}
