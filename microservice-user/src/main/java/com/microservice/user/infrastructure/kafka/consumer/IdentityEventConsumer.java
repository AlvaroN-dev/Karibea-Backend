package com.microservice.user.infrastructure.kafka.consumer;

import com.microservice.user.domain.port.in.DeleteUserProfileUseCase;
import com.microservice.user.infrastructure.adapters.external.IdentityServiceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Consumidor de eventos desde microservice-identity.
 * 
 * Mantiene sincronizado el cache local de usuarios y 
 * reacciona a eventos del ciclo de vida del usuario.
 */
@Component
public class IdentityEventConsumer {
    
    private static final Logger log = LoggerFactory.getLogger(IdentityEventConsumer.class);
    
    private final DeleteUserProfileUseCase deleteUserProfileUseCase;
    private final IdentityServiceAdapter identityServiceAdapter;
    
    public IdentityEventConsumer(DeleteUserProfileUseCase deleteUserProfileUseCase,
                                 IdentityServiceAdapter identityServiceAdapter) {
        this.deleteUserProfileUseCase = deleteUserProfileUseCase;
        this.identityServiceAdapter = identityServiceAdapter;
    }
    
    @KafkaListener(
        topics = "identity-events",
        groupId = "microservice-user-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleIdentityEvent(@Payload Map<String, Object> event,
                                    @Header(value = "eventType", required = false) String eventType,
                                    Acknowledgment ack) {
        try {
            String type = eventType != null ? eventType : (String) event.get("eventType");
            
            if (type == null) {
                log.warn("Received event without eventType: {}", event);
                ack.acknowledge();
                return;
            }
            
            switch (type) {
                case "UserCreated", "USER_CREATED" -> handleUserCreated(event);
                case "UserDeleted", "USER_DELETED" -> handleUserDeleted(event);
                case "UserUpdated", "USER_UPDATED" -> handleUserUpdated(event);
                default -> log.debug("Ignoring event type: {}", type);
            }
            
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing identity event: {}", event, e);
            // No acknowledge para reintentar
        }
    }
    
    private void handleUserCreated(Map<String, Object> event) {
        try {
            String userIdStr = extractUserId(event);
            if (userIdStr == null) {
                log.warn("UserCreated event missing userId: {}", event);
                return;
            }
            
            java.util.UUID userId = java.util.UUID.fromString(userIdStr);
            String username = (String) event.get("username");
            String email = (String) event.get("email");
            
            // Actualizar cache local
            identityServiceAdapter.updateUserCache(
                userId,
                username,
                email,
                true,  // enabled
                false, // emailVerified (default)
                false  // isVerified (default)
            );
            
            log.info("Processed UserCreated event for userId: {}", userId);
        } catch (Exception e) {
            log.error("Error handling UserCreated event: {}", event, e);
        }
    }
    
    private void handleUserDeleted(Map<String, Object> event) {
        try {
            String userIdStr = extractUserId(event);
            if (userIdStr == null) {
                log.warn("UserDeleted event missing userId: {}", event);
                return;
            }
            
            java.util.UUID userId = java.util.UUID.fromString(userIdStr);
            
            // Eliminar perfil de usuario
            try {
                deleteUserProfileUseCase.executeByExternalUserId(userId);
                log.info("User profile deleted for userId: {}", userId);
            } catch (Exception e) {
                log.warn("No profile found to delete for userId: {}", userId);
            }
            
            // Eliminar del cache
            identityServiceAdapter.removeFromCache(userId);
            
            log.info("Processed UserDeleted event for userId: {}", userId);
        } catch (Exception e) {
            log.error("Error handling UserDeleted event: {}", event, e);
        }
    }
    
    private void handleUserUpdated(Map<String, Object> event) {
        try {
            String userIdStr = extractUserId(event);
            if (userIdStr == null) {
                log.warn("UserUpdated event missing userId: {}", event);
                return;
            }
            
            java.util.UUID userId = java.util.UUID.fromString(userIdStr);
            String username = (String) event.get("username");
            String email = (String) event.get("email");
            Boolean enabled = (Boolean) event.getOrDefault("enabled", true);
            Boolean emailVerified = (Boolean) event.getOrDefault("emailVerified", false);
            Boolean isVerified = (Boolean) event.getOrDefault("isVerified", false);
            
            // Actualizar cache local
            identityServiceAdapter.updateUserCache(
                userId,
                username,
                email,
                enabled != null ? enabled : true,
                emailVerified != null ? emailVerified : false,
                isVerified != null ? isVerified : false
            );
            
            log.info("Processed UserUpdated event for userId: {}", userId);
        } catch (Exception e) {
            log.error("Error handling UserUpdated event: {}", event, e);
        }
    }
    
    private String extractUserId(Map<String, Object> event) {
        Object userId = event.get("userId");
        if (userId == null) {
            userId = event.get("aggregateId");
        }
        return userId != null ? userId.toString() : null;
    }
}
