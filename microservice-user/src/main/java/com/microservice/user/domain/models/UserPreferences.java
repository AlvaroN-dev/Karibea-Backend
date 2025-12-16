package com.microservice.user.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad de dominio - Preferencias del usuario
 */
public class UserPreferences {
    
    private final UUID id;
    private final UUID externalUserId;
    private Language language;
    private Currency currency;
    private boolean notificationEmail;
    private boolean notificationPush;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private boolean deleted;
    
    private UserPreferences(UUID id, UUID externalUserId, Language language, Currency currency,
                            boolean notificationEmail, boolean notificationPush,
                            LocalDateTime createdAt, LocalDateTime updatedAt,
                            LocalDateTime deletedAt, boolean deleted) {
        this.id = id;
        this.externalUserId = externalUserId;
        this.language = language;
        this.currency = currency;
        this.notificationEmail = notificationEmail;
        this.notificationPush = notificationPush;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.deleted = deleted;
    }
    
    /**
     * Factory method para crear preferencias con valores por defecto
     */
    public static UserPreferences createDefault(UUID externalUserId) {
        Objects.requireNonNull(externalUserId, "External user ID cannot be null");
        
        LocalDateTime now = LocalDateTime.now();
        return new UserPreferences(
            UUID.randomUUID(),
            externalUserId,
            null,
            null,
            true,  // notificaciones por email habilitadas por defecto
            true,  // notificaciones push habilitadas por defecto
            now,
            now,
            null,
            false
        );
    }
    
    /**
     * Factory method para reconstruir desde persistencia
     */
    public static UserPreferences reconstitute(UUID id, UUID externalUserId, Language language,
                                               Currency currency, boolean notificationEmail,
                                               boolean notificationPush, LocalDateTime createdAt,
                                               LocalDateTime updatedAt, LocalDateTime deletedAt,
                                               boolean deleted) {
        return new UserPreferences(id, externalUserId, language, currency, notificationEmail,
                                   notificationPush, createdAt, updatedAt, deletedAt, deleted);
    }
    
    // Métodos de dominio
    
    public void updateLanguage(Language language) {
        this.language = language;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateCurrency(Currency currency) {
        this.currency = currency;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateNotificationSettings(boolean emailEnabled, boolean pushEnabled) {
        this.notificationEmail = emailEnabled;
        this.notificationPush = pushEnabled;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void enableEmailNotifications() {
        this.notificationEmail = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void disableEmailNotifications() {
        this.notificationEmail = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void enablePushNotifications() {
        this.notificationPush = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void disablePushNotifications() {
        this.notificationPush = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markAsDeleted() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters
    
    public UUID getId() {
        return id;
    }
    
    public UUID getExternalUserId() {
        return externalUserId;
    }
    
    public Language getLanguage() {
        return language;
    }
    
    public Currency getCurrency() {
        return currency;
    }
    
    public boolean isNotificationEmail() {
        return notificationEmail;
    }
    
    public boolean isNotificationPush() {
        return notificationPush;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
    
    public boolean isDeleted() {
        return deleted;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPreferences that = (UserPreferences) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
