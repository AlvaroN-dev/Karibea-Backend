package com.microservice.user.infrastructure.kafka.consumer;

import com.microservice.user.domain.port.in.DeleteUserProfileUseCase;
import com.microservice.user.infrastructure.kafka.events.UserCreatedEvent;
import com.microservice.user.infrastructure.kafka.events.UserDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor de eventos desde microservice-identity
 */
@Component
public class IdentityEventConsumer {
    
    private static final Logger log = LoggerFactory.getLogger(IdentityEventConsumer.class);
    
    private final DeleteUserProfileUseCase deleteUserProfileUseCase;
    
    public IdentityEventConsumer(DeleteUserProfileUseCase deleteUserProfileUseCase) {
        this.deleteUserProfileUseCase = deleteUserProfileUseCase;
    }
    
    @KafkaListener(
        topics = "identity.user.events",
        groupId = "microservice-user-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Received user.created event for userId: {}", event.userId());
        // El perfil se crea cuando el usuario lo solicita, no automáticamente
        // Este evento se puede usar para cache warming o analytics
    }
    
    @KafkaListener(
        topics = "identity.user.events",
        groupId = "microservice-user-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleUserDeletedEvent(UserDeletedEvent event) {
        log.info("Received user.deleted event for userId: {}", event.userId());
        try {
            deleteUserProfileUseCase.executeByExternalUserId(event.userId());
            log.info("User profile deleted for userId: {}", event.userId());
        } catch (Exception e) {
            log.error("Failed to delete user profile for userId: {}", event.userId(), e);
        }
    }
}
