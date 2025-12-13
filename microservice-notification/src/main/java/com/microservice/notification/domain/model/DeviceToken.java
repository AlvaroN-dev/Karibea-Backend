package com.microservice.notification.domain.model;

import java.time.Instant;

public class DeviceToken {
    private Long id;
    private String externalUserId;

    private String deviceToken;
    private String platform;

    private boolean active;
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
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
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
