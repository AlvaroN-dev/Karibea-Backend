package com.microservice.user.infrastructure.entities.mapper;

import com.microservice.user.domain.models.Gender;
import com.microservice.user.domain.models.UserProfile;
import com.microservice.user.infrastructure.entities.GenderEntity;
import com.microservice.user.infrastructure.entities.UserProfileEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper entre entidad JPA y modelo de dominio para UserProfile
 */
@Component
public class UserProfileEntityMapper {
    
    private final GenderEntityMapper genderMapper;
    
    public UserProfileEntityMapper(GenderEntityMapper genderMapper) {
        this.genderMapper = genderMapper;
    }
    
    public UserProfile toDomain(UserProfileEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Gender gender = entity.getGender() != null 
            ? genderMapper.toDomain(entity.getGender()) 
            : null;
        
        return UserProfile.reconstitute(
            entity.getId(),
            entity.getExternalUserId(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getMiddleName(),
            entity.getSecondLastname(),
            entity.getPhone(),
            gender,
            entity.getAvatarUrl(),
            entity.getDateOfBirth(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.isDeleted()
        );
    }
    
    public UserProfileEntity toEntity(UserProfile domain) {
        if (domain == null) {
            return null;
        }
        
        GenderEntity genderEntity = domain.getGender() != null 
            ? genderMapper.toEntity(domain.getGender()) 
            : null;
        
        return UserProfileEntity.builder()
            .id(domain.getId())
            .externalUserId(domain.getExternalUserId())
            .firstName(domain.getFirstName())
            .lastName(domain.getLastName())
            .middleName(domain.getMiddleName())
            .secondLastname(domain.getSecondLastname())
            .phone(domain.getPhone())
            .gender(genderEntity)
            .avatarUrl(domain.getAvatarUrl())
            .dateOfBirth(domain.getDateOfBirth())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .deleted(domain.isDeleted())
            .build();
    }
}
