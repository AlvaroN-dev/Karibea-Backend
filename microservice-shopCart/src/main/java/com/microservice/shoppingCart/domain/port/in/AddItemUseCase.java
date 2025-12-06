package com.microservice.shoppingCart.domain.port.in;


import java.util.UUID;

import com.microservice.shoppingCart.domain.model.Item;

public interface AddItemUseCase {
    void addItem(UUID cartId, Item item);
}