package com.microservice.user.domain.port.out;

import com.microservice.user.domain.models.Language;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto saliente - Repositorio de idiomas
 */
public interface LanguageRepositoryPort {
    
    List<Language> findAll();
    
    Optional<Language> findById(UUID id);
    
    Optional<Language> findByCode(String code);
}
