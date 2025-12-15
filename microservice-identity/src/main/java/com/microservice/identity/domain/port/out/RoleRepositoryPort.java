package com.microservice.identity.domain.port.out;

import com.microservice.identity.domain.models.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepositoryPort {
    Optional<Role> findById(UUID id);
    Optional<Role> findByName(String name);
    List<Role> findAll();
    Role save(Role role);
    void delete(Role role);
    boolean existsByName(String name);
}
