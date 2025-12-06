package com.microservice.shoppingCart.domain.port.in;

import com.microservice.shoppingCart.domain.model.ShoppingCart;

public interface CreateCartUseCase {
    ShoppingCart create(ShoppingCart cart);
}
