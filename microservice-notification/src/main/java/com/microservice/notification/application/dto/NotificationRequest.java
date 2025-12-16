package com.microservice.notification.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Schema(description = "Solicitud para crear una nueva notificación")
public class NotificationRequest {

    @Schema(description = "ID externo del perfil de usuario", example = "user-profile-123")
    @NotBlank
    private String externalUserProfileId;

    @Schema(description = "ID de la plantilla de notificación (opcional)", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID templateId;

    @Schema(description = "Canal de notificación: EMAIL, SMS, PUSH, IN_APP", example = "EMAIL")
    @NotBlank
    private String channel;

    @Schema(description = "Título de la notificación", example = "Tu pedido ha sido confirmado")
    @NotBlank
    private String title;

    @Schema(description = "Contenido del mensaje", example = "Tu pedido #12345 ha sido procesado exitosamente")
    @NotBlank
    private String message;

    @Schema(description = "Email del destinatario (requerido para canal EMAIL)", example = "usuario@ejemplo.com")
    private String recipientEmail;

    @Schema(description = "Teléfono del destinatario (requerido para canal SMS)", example = "+1234567890")
    private String recipientPhone;

    @Schema(description = "Token del dispositivo (requerido para canal PUSH)")
    private String recipientDeviceToken;

    @Schema(description = "Prioridad de la notificación: LOW, NORMAL, HIGH, URGENT", example = "NORMAL")
    private String priority;

    @Schema(description = "Tipo de referencia asociada: ORDER, PAYMENT, SHIPPING, etc.", example = "ORDER")
    private String referenceType;

    @Schema(description = "ID de la referencia externa", example = "order-12345")
    private String externalReferenceId;

    @Schema(description = "URL de acción para la notificación", example = "https://app.example.com/orders/12345")
    private String actionUrl;

    @Schema(description = "Metadatos adicionales en formato JSON", example = "{\"orderId\": \"12345\", \"total\": 99.99}")
    private String metadata;

    // Getters and Setters

    public String getExternalUserProfileId() {
        return externalUserProfileId;
    }

    public void setExternalUserProfileId(String externalUserProfileId) {
        this.externalUserProfileId = externalUserProfileId;
    }

    public UUID getTemplateId() {
        return templateId;
    }

    public void setTemplateId(UUID templateId) {
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
