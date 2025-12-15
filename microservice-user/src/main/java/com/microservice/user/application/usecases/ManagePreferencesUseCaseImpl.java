package com.microservice.user.application.usecases;

import com.microservice.user.domain.events.PreferencesUpdatedEvent;
import com.microservice.user.domain.exceptions.PreferencesNotFoundException;
import com.microservice.user.domain.models.Currency;
import com.microservice.user.domain.models.Language;
import com.microservice.user.domain.models.UserPreferences;
import com.microservice.user.domain.port.in.ManagePreferencesUseCase;
import com.microservice.user.domain.port.out.CurrencyRepositoryPort;
import com.microservice.user.domain.port.out.EventPublisherPort;
import com.microservice.user.domain.port.out.LanguageRepositoryPort;
import com.microservice.user.domain.port.out.PreferencesRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementación del caso de uso para gestionar preferencias del usuario
 */
@Service
@Transactional
public class ManagePreferencesUseCaseImpl implements ManagePreferencesUseCase {
    
    private final PreferencesRepositoryPort preferencesRepository;
    private final LanguageRepositoryPort languageRepository;
    private final CurrencyRepositoryPort currencyRepository;
    private final EventPublisherPort eventPublisher;
    
    public ManagePreferencesUseCaseImpl(PreferencesRepositoryPort preferencesRepository,
                                        LanguageRepositoryPort languageRepository,
                                        CurrencyRepositoryPort currencyRepository,
                                        EventPublisherPort eventPublisher) {
        this.preferencesRepository = preferencesRepository;
        this.languageRepository = languageRepository;
        this.currencyRepository = currencyRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Override
    public UserPreferences create(UUID externalUserId) {
        // Verificar si ya existen preferencias
        if (preferencesRepository.existsByExternalUserId(externalUserId)) {
            return preferencesRepository.findByExternalUserId(externalUserId).orElseThrow();
        }
        
        UserPreferences preferences = UserPreferences.createDefault(externalUserId);
        return preferencesRepository.save(preferences);
    }
    
    @Override
    public UserPreferences update(UpdatePreferencesCommand command) {
        UserPreferences preferences = preferencesRepository.findByExternalUserId(command.externalUserId())
            .orElseThrow(() -> new PreferencesNotFoundException(command.externalUserId()));
        
        // Actualizar idioma
        if (command.languageId() != null) {
            Language language = languageRepository.findById(command.languageId()).orElse(null);
            preferences.updateLanguage(language);
        }
        
        // Actualizar moneda
        if (command.currencyId() != null) {
            Currency currency = currencyRepository.findById(command.currencyId()).orElse(null);
            preferences.updateCurrency(currency);
        }
        
        // Actualizar notificaciones
        if (command.notificationEmail() != null || command.notificationPush() != null) {
            preferences.updateNotificationSettings(
                command.notificationEmail() != null ? command.notificationEmail() : preferences.isNotificationEmail(),
                command.notificationPush() != null ? command.notificationPush() : preferences.isNotificationPush()
            );
        }
        
        UserPreferences saved = preferencesRepository.save(preferences);
        
        // Publicar evento
        eventPublisher.publish(PreferencesUpdatedEvent.create(
            saved.getId(),
            saved.getExternalUserId(),
            saved.getLanguage() != null ? saved.getLanguage().getId() : null,
            saved.getCurrency() != null ? saved.getCurrency().getId() : null,
            saved.isNotificationEmail(),
            saved.isNotificationPush()
        ));
        
        return saved;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<UserPreferences> findByExternalUserId(UUID externalUserId) {
        return preferencesRepository.findByExternalUserId(externalUserId);
    }
    
    @Override
    public void delete(UUID externalUserId) {
        preferencesRepository.deleteByExternalUserId(externalUserId);
    }
}
