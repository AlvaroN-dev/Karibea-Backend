package com.microservice.user.infrastructure.entities.mapper;

import com.microservice.user.domain.models.Gender;
import com.microservice.user.infrastructure.entities.GenderEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper entre entidad JPA y modelo de dominio para Gender
 */
@Component
public class GenderEntityMapper {
    
    public Gender toDomain(GenderEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Gender.reconstitute(
            entity.getId(),
            entity.getName(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
    
    public GenderEntity toEntity(Gender domain) {
        if (domain == null) {
            return null;
        }
        
        return GenderEntity.builder()
            .id(domain.getId())
            .name(domain.getName())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .build();
    }
    
    public List<Gender> toDomainList(List<GenderEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
}
