package com.microservice.shoppingCart.domain.port.in;

import java.util.UUID;

public interface RemoveItemUseCase {
    void removeItem(UUID cartId, UUID itemId);
}
