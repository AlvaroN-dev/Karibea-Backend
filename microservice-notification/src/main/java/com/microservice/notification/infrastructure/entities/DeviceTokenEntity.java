package com.microservice.notification.infrastructure.entities;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "device_tokens")
public class DeviceTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_user_id", nullable = false)
    private String externalUserId;

    @Column(columnDefinition = "text", nullable = false)
    private String deviceToken;

    private String platform;
    private boolean isActive;

    private Instant lastUsedAt;
    private Instant createdAt;
    private Instant updatedAt;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getExternalUserId() {
        return externalUserId;
    }
    public void setExternalUserId(String externalUserId) {
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

