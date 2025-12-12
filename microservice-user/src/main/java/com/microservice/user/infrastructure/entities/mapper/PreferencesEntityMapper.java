package com.microservice.user.infrastructure.entities.mapper;

import com.microservice.user.domain.models.Currency;
import com.microservice.user.domain.models.Language;
import com.microservice.user.domain.models.UserPreferences;
import com.microservice.user.infrastructure.entities.UserPreferencesEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper entre entidad JPA y modelo de dominio para UserPreferences
 */
@Component
public class PreferencesEntityMapper {
    
    private final LanguageEntityMapper languageMapper;
    private final CurrencyEntityMapper currencyMapper;
    
    public PreferencesEntityMapper(LanguageEntityMapper languageMapper, 
                                   CurrencyEntityMapper currencyMapper) {
        this.languageMapper = languageMapper;
        this.currencyMapper = currencyMapper;
    }
    
    public UserPreferences toDomain(UserPreferencesEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Language language = entity.getLanguage() != null 
            ? languageMapper.toDomain(entity.getLanguage()) 
            : null;
        
        Currency currency = entity.getCurrency() != null 
            ? currencyMapper.toDomain(entity.getCurrency()) 
            : null;
        
        return UserPreferences.reconstitute(
            entity.getId(),
            entity.getExternalUserId(),
            language,
            currency,
            entity.isNotificationEmail(),
            entity.isNotificationPush(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getDeletedAt(),
            entity.isDeleted()
        );
    }
    
    public UserPreferencesEntity toEntity(UserPreferences domain) {
        if (domain == null) {
            return null;
        }
        
        return UserPreferencesEntity.builder()
            .id(domain.getId())
            .externalUserId(domain.getExternalUserId())
            .language(domain.getLanguage() != null 
                ? languageMapper.toEntity(domain.getLanguage()) 
                : null)
            .currency(domain.getCurrency() != null 
                ? currencyMapper.toEntity(domain.getCurrency()) 
                : null)
            .notificationEmail(domain.isNotificationEmail())
            .notificationPush(domain.isNotificationPush())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .deletedAt(domain.getDeletedAt())
            .deleted(domain.isDeleted())
            .build();
    }
}
