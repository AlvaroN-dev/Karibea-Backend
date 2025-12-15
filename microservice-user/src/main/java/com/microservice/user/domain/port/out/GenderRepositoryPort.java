package com.microservice.user.domain.port.out;

import com.microservice.user.domain.models.Gender;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto saliente - Repositorio de géneros
 */
public interface GenderRepositoryPort {
    
    List<Gender> findAll();
    
    Optional<Gender> findById(UUID id);
    
    Optional<Gender> findByName(String name);
}
