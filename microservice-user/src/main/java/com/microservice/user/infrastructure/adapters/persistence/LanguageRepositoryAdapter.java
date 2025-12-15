package com.microservice.user.infrastructure.adapters.persistence;

import com.microservice.user.domain.models.Language;
import com.microservice.user.domain.port.out.LanguageRepositoryPort;
import com.microservice.user.infrastructure.entities.mapper.LanguageEntityMapper;
import com.microservice.user.infrastructure.repositories.JpaLanguageRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia para idiomas
 */
@Component
public class LanguageRepositoryAdapter implements LanguageRepositoryPort {
    
    private final JpaLanguageRepository jpaRepository;
    private final LanguageEntityMapper mapper;
    
    public LanguageRepositoryAdapter(JpaLanguageRepository jpaRepository,
                                     LanguageEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public List<Language> findAll() {
        return mapper.toDomainList(jpaRepository.findAll());
    }
    
    @Override
    public Optional<Language> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Language> findByCode(String code) {
        return jpaRepository.findByCode(code)
            .map(mapper::toDomain);
    }
}
