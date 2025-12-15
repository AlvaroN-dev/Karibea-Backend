package com.microservice.notification.application.dto;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de preferencias de notificación del usuario")
public class UserPreferencesResponse {
    
    @Schema(description = "Identificador único de las preferencias", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    
    @Schema(description = "ID externo del usuario", example = "user-123")
    private String externalUserId;
    
    @Schema(description = "Notificaciones por email habilitadas")
    private boolean emailEnabled;
    
    @Schema(description = "Notificaciones push habilitadas")
    private boolean pushEnabled;
    
    @Schema(description = "Notificaciones in-app habilitadas")
    private boolean inAppEnabled;
    
    @Schema(description = "Preferencias adicionales en formato JSON")
    private String preferences;
    
    @Schema(description = "Hora de inicio del horario silencioso", example = "22:00:00")
    private LocalTime quietHoursStart;
    
    @Schema(description = "Hora de fin del horario silencioso", example = "08:00:00")
    private LocalTime quietHoursEnd;
    
    @Schema(description = "Zona horaria del usuario", example = "America/Bogota")
    private String timezone;
    
    @Schema(description = "Fecha de creación")
    private Instant createdAt;
    
    @Schema(description = "Fecha de última actualización")
    private Instant updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public boolean isPushEnabled() {
        return pushEnabled;
    }

    public void setPushEnabled(boolean pushEnabled) {
        this.pushEnabled = pushEnabled;
    }

    public boolean isInAppEnabled() {
        return inAppEnabled;
    }

    public void setInAppEnabled(boolean inAppEnabled) {
        this.inAppEnabled = inAppEnabled;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public LocalTime getQuietHoursStart() {
        return quietHoursStart;
    }

    public void setQuietHoursStart(LocalTime quietHoursStart) {
        this.quietHoursStart = quietHoursStart;
    }

    public LocalTime getQuietHoursEnd() {
        return quietHoursEnd;
    }

    public void setQuietHoursEnd(LocalTime quietHoursEnd) {
        this.quietHoursEnd = quietHoursEnd;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
