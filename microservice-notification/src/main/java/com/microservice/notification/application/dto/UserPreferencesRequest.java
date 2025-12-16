package com.microservice.notification.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

@Schema(description = "Solicitud para crear o actualizar preferencias de notificación")
public class UserPreferencesRequest {

    @Schema(description = "ID externo del usuario", example = "user-123")
    private String externalUserId;

    @Schema(description = "Habilitar notificaciones por email", example = "true")
    private boolean emailEnabled;

    @Schema(description = "Habilitar notificaciones push", example = "true")
    private boolean pushEnabled;

    @Schema(description = "Habilitar notificaciones in-app", example = "true")
    private boolean inAppEnabled;

    @Schema(description = "Preferencias adicionales en formato JSON")
    private String preferences;

    @Schema(description = "Hora de inicio para no molestar", example = "22:00:00")
    private LocalTime quietHoursStart;

    @Schema(description = "Hora de fin para no molestar", example = "08:00:00")
    private LocalTime quietHoursEnd;

    @Schema(description = "Zona horaria del usuario", example = "America/Bogota")
    private String timezone;

    // Getters and Setters

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
}

