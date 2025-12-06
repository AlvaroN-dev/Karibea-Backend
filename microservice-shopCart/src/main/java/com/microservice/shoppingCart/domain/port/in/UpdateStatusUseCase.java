package com.microservice.shoppingCart.domain.port.in;

import java.util.UUID;

import com.microservice.shoppingCart.domain.model.Status;

public interface UpdateStatusUseCase {
    void updateStatus(UUID cartId, Status status);
}
