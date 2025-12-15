package com.microservice.notification.application.dto;

import java.time.Instant;
import java.util.Map;

public class NotificationRequest {
    private String externalUserProfileId;
    private Long templateId;
    private String channel;
    private String title;
    private String message;
    private String recipientEmail;
    private String recipientPhone;
    private String recipientDeviceToken;
    private String priority;
    private String referenceType;
    private String externalReferenceId;
    private String actionUrl;
    private String metadata; // Keeping as String for simplicity, could be Map

    // Getters and Setters
    public String getExternalUserProfileId() {
        return externalUserProfileId;
    }

    public void setExternalUserProfileId(String externalUserProfileId) {
        this.externalUserProfileId = externalUserProfileId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getRecipientDeviceToken() {
        return recipientDeviceToken;
    }

    public void setRecipientDeviceToken(String recipientDeviceToken) {
        this.recipientDeviceToken = recipientDeviceToken;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getExternalReferenceId() {
        return externalReferenceId;
    }

    public void setExternalReferenceId(String externalReferenceId) {
        this.externalReferenceId = externalReferenceId;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
