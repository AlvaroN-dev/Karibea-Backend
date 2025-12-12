package com.microservice.user.infrastructure.entities.mapper;

import com.microservice.user.domain.models.Currency;
import com.microservice.user.infrastructure.entities.CurrencyEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper entre entidad JPA y modelo de dominio para Currency
 */
@Component
public class CurrencyEntityMapper {
    
    public Currency toDomain(CurrencyEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Currency.reconstitute(
            entity.getId(),
            entity.getName(),
            entity.getCode(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
    
    public CurrencyEntity toEntity(Currency domain) {
        if (domain == null) {
            return null;
        }
        
        return CurrencyEntity.builder()
            .id(domain.getId())
            .name(domain.getName())
            .code(domain.getCode())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .build();
    }
    
    public List<Currency> toDomainList(List<CurrencyEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
}
