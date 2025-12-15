package com.microservice.user.infrastructure.entities.mapper;

import com.microservice.user.domain.models.Address;
import com.microservice.user.infrastructure.entities.AddressEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper entre entidad JPA y modelo de dominio para Address
 */
@Component
public class AddressEntityMapper {
    
    public Address toDomain(AddressEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Address.reconstitute(
            entity.getId(),
            entity.getExternalUserId(),
            entity.getLabel(),
            entity.getStreetAddress(),
            entity.getCity(),
            entity.getState(),
            entity.getPostalCode(),
            entity.getCountry(),
            entity.isDefault(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getDeletedAt(),
            entity.isDeleted()
        );
    }
    
    public AddressEntity toEntity(Address domain) {
        if (domain == null) {
            return null;
        }
        
        return AddressEntity.builder()
            .id(domain.getId())
            .externalUserId(domain.getExternalUserId())
            .label(domain.getLabel())
            .streetAddress(domain.getStreetAddress())
            .city(domain.getCity())
            .state(domain.getState())
            .postalCode(domain.getPostalCode())
            .country(domain.getCountry())
            .isDefault(domain.isDefault())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .deletedAt(domain.getDeletedAt())
            .deleted(domain.isDeleted())
            .build();
    }
    
    public List<Address> toDomainList(List<AddressEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
}
