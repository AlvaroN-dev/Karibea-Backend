package com.microservice.identity.domain.port.out;

import com.microservice.identity.domain.models.AccessLevel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccessLevelRepositoryPort {
    Optional<AccessLevel> findById(UUID id);

    Optional<AccessLevel> findByName(String name);

    List<AccessLevel> findAll();

    AccessLevel save(AccessLevel accessLevel);

    void delete(AccessLevel accessLevel);
}
