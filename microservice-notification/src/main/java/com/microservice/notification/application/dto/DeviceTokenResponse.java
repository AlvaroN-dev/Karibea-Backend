package com.microservice.notification.application.dto;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de token de dispositivo")
public class DeviceTokenResponse {
    
    @Schema(description = "Identificador único del token", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    
    @Schema(description = "ID externo del usuario", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID externalUserId;
    
    @Schema(description = "Token del dispositivo para push notifications")
    private String deviceToken;
    
    @Schema(description = "Plataforma del dispositivo", example = "ANDROID")
    private String platform;
    
    @Schema(description = "Indica si el token está activo")
    private boolean isActive;
    
    @Schema(description = "Última vez que se usó el token")
    private Instant lastUsedAt;
    
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

    public UUID getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(UUID externalUserId) {
        this.externalUserId = externalUserId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Instant getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(Instant lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
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
