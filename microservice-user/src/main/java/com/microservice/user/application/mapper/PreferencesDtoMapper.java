package com.microservice.user.application.mapper;

import com.microservice.user.application.dto.response.CurrencyResponse;
import com.microservice.user.application.dto.response.LanguageResponse;
import com.microservice.user.application.dto.response.UserPreferencesResponse;
import com.microservice.user.domain.models.Currency;
import com.microservice.user.domain.models.Language;
import com.microservice.user.domain.models.UserPreferences;
import org.springframework.stereotype.Component;

/**
 * Mapper entre dominio y DTOs para preferencias
 */
@Component
public class PreferencesDtoMapper {
    
    public UserPreferencesResponse toResponse(UserPreferences preferences) {
        if (preferences == null) {
            return null;
        }
        
        return new UserPreferencesResponse(
            preferences.getId(),
            preferences.getExternalUserId(),
            toLanguageResponse(preferences.getLanguage()),
            toCurrencyResponse(preferences.getCurrency()),
            preferences.isNotificationEmail(),
            preferences.isNotificationPush(),
            preferences.getCreatedAt(),
            preferences.getUpdatedAt()
        );
    }
    
    public LanguageResponse toLanguageResponse(Language language) {
        if (language == null) {
            return null;
        }
        return new LanguageResponse(language.getId(), language.getName(), language.getCode());
    }
    
    public CurrencyResponse toCurrencyResponse(Currency currency) {
        if (currency == null) {
            return null;
        }
        return new CurrencyResponse(currency.getId(), currency.getName(), currency.getCode());
    }
}
