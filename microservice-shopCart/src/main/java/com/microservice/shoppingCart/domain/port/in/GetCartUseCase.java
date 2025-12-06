package com.microservice.shoppingCart.domain.port.in;

import java.util.UUID;

import com.microservice.shoppingCart.domain.model.ShoppingCart;

public interface GetCartUseCase {
    ShoppingCart getById(UUID cartId);
}
