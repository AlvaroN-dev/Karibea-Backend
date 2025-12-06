package com.microservice.shoppingCart.domain.port.out;

import java.util.Optional;
import java.util.UUID;

import com.microservice.shoppingCart.domain.model.Status;

public interface StatusRepositoryPort {
    Optional<Status> findById(UUID id);
    Optional<Status> findByName(String name);
}
