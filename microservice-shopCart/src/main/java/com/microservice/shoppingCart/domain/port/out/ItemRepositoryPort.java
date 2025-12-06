package com.microservice.shoppingCart.domain.port.out;

import java.util.UUID;

import com.microservice.shoppingCart.domain.model.Item;

public interface ItemRepositoryPort {
    Item save(Item item);
    void deleteById(UUID itemId);
    // Depending on implementation, you may need findById, findByCartId, etc.
    java.util.Optional<Item> findById(UUID itemId);
}
