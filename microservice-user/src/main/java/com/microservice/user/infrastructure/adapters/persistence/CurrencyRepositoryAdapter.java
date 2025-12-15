package com.microservice.user.infrastructure.adapters.persistence;

import com.microservice.user.domain.models.Currency;
import com.microservice.user.domain.port.out.CurrencyRepositoryPort;
import com.microservice.user.infrastructure.entities.mapper.CurrencyEntityMapper;
import com.microservice.user.infrastructure.repositories.JpaCurrencyRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia para monedas
 */
@Component
public class CurrencyRepositoryAdapter implements CurrencyRepositoryPort {
    
    private final JpaCurrencyRepository jpaRepository;
    private final CurrencyEntityMapper mapper;
    
    public CurrencyRepositoryAdapter(JpaCurrencyRepository jpaRepository,
                                     CurrencyEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public List<Currency> findAll() {
        return mapper.toDomainList(jpaRepository.findAll());
    }
    
    @Override
    public Optional<Currency> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Currency> findByCode(String code) {
        return jpaRepository.findByCode(code)
            .map(mapper::toDomain);
    }
}
