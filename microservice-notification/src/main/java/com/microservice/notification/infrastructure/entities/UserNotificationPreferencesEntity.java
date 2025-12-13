package com.microservice.notification.infrastructure.entities;


import java.time.Instant;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "user_notification_preferences")
public class UserNotificationPreferencesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_user_id", unique = true, nullable = false)
    private String externalUserId;

    private boolean emailEnabled;
    private boolean pushEnabled;
    private boolean inAppEnabled;

    @Column(columnDefinition = "jsonb")
    private String preferences;

    private LocalTime quietHoursStart;
    private LocalTime quietHoursEnd;
    private String timezone;

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

