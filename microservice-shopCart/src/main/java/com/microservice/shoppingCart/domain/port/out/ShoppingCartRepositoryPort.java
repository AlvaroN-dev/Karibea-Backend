package com.microservice.shoppingCart.domain.port.out;

import java.util.Optional;
import java.util.UUID;

import com.microservice.shoppingCart.domain.model.ShoppingCart;

public interface ShoppingCartRepositoryPort {
    ShoppingCart save(ShoppingCart cart);
    Optional<ShoppingCart> findById(UUID id);
    void deleteById(UUID id);
}