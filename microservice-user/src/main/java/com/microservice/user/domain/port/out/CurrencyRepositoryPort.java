package com.microservice.user.domain.port.out;

import com.microservice.user.domain.models.Currency;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto saliente - Repositorio de monedas
 */
public interface CurrencyRepositoryPort {
    
    List<Currency> findAll();
    
    Optional<Currency> findById(UUID id);
    
    Optional<Currency> findByCode(String code);
}
