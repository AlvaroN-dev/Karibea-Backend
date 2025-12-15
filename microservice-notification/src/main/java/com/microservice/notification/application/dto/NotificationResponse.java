package com.microservice.notification.application.dto;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de notificación con información completa")
public class NotificationResponse {
    
    @Schema(description = "Identificador único de la notificación", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    
    @Schema(description = "ID externo del perfil de usuario", example = "user-profile-123")
    private String externalUserProfileId;
    
    @Schema(description = "Canal de notificación", example = "EMAIL")
    private String channel;
    
    @Schema(description = "Título de la notificación", example = "Tu pedido ha sido confirmado")
    private String title;
    
    @Schema(description = "Contenido del mensaje", example = "Tu pedido #12345 ha sido procesado exitosamente")
    private String message;
    
    @Schema(description = "Estado de la notificación")
    private NotificationStatusInfo status;
    
    @Schema(description = "Plantilla utilizada (si aplica)")
    private NotificationTemplateInfo template;
    
    @Schema(description = "Email del destinatario", example = "usuario@ejemplo.com")
    private String recipientEmail;
    
    @Schema(description = "Teléfono del destinatario", example = "+1234567890")
    private String recipientPhone;
    
    @Schema(description = "Prioridad de la notificación", example = "HIGH")
    private String priority;
    
    @Schema(description = "Tipo de referencia", example = "ORDER")
    private String referenceType;
    
    @Schema(description = "ID de referencia externa", example = "order-12345")
    private String externalReferenceId;
    
    @Schema(description = "URL de acción", example = "https://app.example.com/orders/12345")
    private String actionUrl;
    
    @Schema(description = "Metadatos adicionales en formato JSON")
    private String metadata;
    
    @Schema(description = "Fecha de envío")
    private Instant sentAt;
    
    @Schema(description = "Fecha de entrega")
    private Instant deliveredAt;
    
    @Schema(description = "Fecha de lectura")
    private Instant readAt;
    
    @Schema(description = "Fecha de creación")
    private Instant createdAt;

    // Inner class for embedded status info
    @Schema(description = "Información del estado de la notificación")
    public static class NotificationStatusInfo {
        @Schema(description = "ID del estado", example = "550e8400-e29b-41d4-a716-446655440001")
        private UUID id;
        
        @Schema(description = "Nombre del estado", example = "SENT")
        private String name;
        
        @Schema(description = "Descripción del estado", example = "La notificación ha sido enviada")
        private String description;

        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    // Inner class for embedded template info
    @Schema(description = "Información de la plantilla de notificación")
    public static class NotificationTemplateInfo {
        @Schema(description = "ID de la plantilla", example = "550e8400-e29b-41d4-a716-446655440002")
        private UUID id;
        
        @Schema(description = "Código de la plantilla", example = "ORDER_CONFIRMATION")
        private String code;
        
        @Schema(description = "Nombre de la plantilla", example = "Confirmación de Pedido")
        private String name;
        
        @Schema(description = "Canal de la plantilla", example = "EMAIL")
        private String channel;

        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getChannel() { return channel; }
        public void setChannel(String channel) { this.channel = channel; }
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getExternalUserProfileId() {
        return externalUserProfileId;
    }

    public void setExternalUserProfileId(String externalUserProfileId) {
        this.externalUserProfileId = externalUserProfileId;
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

    public NotificationStatusInfo getStatus() {
        return status;
    }

    public void setStatus(NotificationStatusInfo status) {
        this.status = status;
    }

    public NotificationTemplateInfo getTemplate() {
        return template;
    }

    public void setTemplate(NotificationTemplateInfo template) {
        this.template = template;
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

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public Instant getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(Instant deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
