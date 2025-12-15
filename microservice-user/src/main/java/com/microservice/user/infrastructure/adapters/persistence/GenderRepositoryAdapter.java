package com.microservice.user.infrastructure.adapters.persistence;

import com.microservice.user.domain.models.Gender;
import com.microservice.user.domain.port.out.GenderRepositoryPort;
import com.microservice.user.infrastructure.entities.mapper.GenderEntityMapper;
import com.microservice.user.infrastructure.repositories.JpaGenderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia para géneros
 */
@Component
public class GenderRepositoryAdapter implements GenderRepositoryPort {
    
    private final JpaGenderRepository jpaRepository;
    private final GenderEntityMapper mapper;
    
    public GenderRepositoryAdapter(JpaGenderRepository jpaRepository,
                                   GenderEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public List<Gender> findAll() {
        return mapper.toDomainList(jpaRepository.findAll());
    }
    
    @Override
    public Optional<Gender> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Gender> findByName(String name) {
        return jpaRepository.findByName(name)
            .map(mapper::toDomain);
    }
}
