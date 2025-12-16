package com.microservice.user.infrastructure.entities.mapper;

import com.microservice.user.domain.models.Language;
import com.microservice.user.infrastructure.entities.LanguageEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper entre entidad JPA y modelo de dominio para Language
 */
@Component
public class LanguageEntityMapper {
    
    public Language toDomain(LanguageEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Language.reconstitute(
            entity.getId(),
            entity.getName(),
            entity.getCode(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
    
    public LanguageEntity toEntity(Language domain) {
        if (domain == null) {
            return null;
        }
        
        return LanguageEntity.builder()
            .id(domain.getId())
            .name(domain.getName())
            .code(domain.getCode())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .build();
    }
    
    public List<Language> toDomainList(List<LanguageEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
}
