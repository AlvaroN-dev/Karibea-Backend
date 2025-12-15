package com.microservice.user.infrastructure.adapters.external;

import com.microservice.user.domain.port.out.IdentityServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adaptador para información del servicio de identidad.
 * 
 * En un sistema event-driven, este adapter mantiene un cache local
 * de usuarios conocidos a través de eventos Kafka recibidos.
 * 
 * La verificación de identidad real se delega al JWT token validado
 * por Spring Security OAuth2 Resource Server.
 */
@Component
public class IdentityServiceAdapter implements IdentityServicePort {
    
    private static final Logger log = LoggerFactory.getLogger(IdentityServiceAdapter.class);
    
    // Cache local de usuarios conocidos (poblado por eventos Kafka)
    private final Map<UUID, UserStatus> userCache = new ConcurrentHashMap<>();
    
    @Override
    public boolean userExists(UUID userId) {
        // En arquitectura event-driven, confiamos en:
        // 1. El JWT token ya fue validado por Spring Security
        // 2. El external_user_id viene del token autenticado
        // Por lo tanto, si llegamos aquí, el usuario existe y está autenticado
        log.debug("User existence check for userId: {} - trusted via JWT", userId);
        return true;
    }
    
    @Override
    public boolean isUserEnabled(UUID userId) {
        // Si el JWT es válido, el usuario está habilitado
        return getUserStatus(userId)
            .map(UserStatus::enabled)
            .orElse(true); // Default: habilitado si tiene JWT válido
    }
    
    @Override
    public Optional<UserStatus> getUserStatus(UUID userId) {
        // Buscar en cache local (poblado por eventos)
        UserStatus cached = userCache.get(userId);
        if (cached != null) {
            log.debug("User status found in cache for userId: {}", userId);
            return Optional.of(cached);
        }
        
        // Si no está en cache, crear status básico desde JWT
        log.debug("User status not in cache for userId: {}, creating default", userId);
        return Optional.of(new UserStatus(
            userId,
            null, // username no disponible sin evento
            null, // email no disponible sin evento
            true, // habilitado (tiene JWT válido)
            true, // email verificado (asumido)
            true  // verificado (asumido)
        ));
    }
    
    /**
     * Actualiza el cache con información de usuario desde evento Kafka
     */
    public void updateUserCache(UUID userId, String username, String email, 
                                boolean enabled, boolean emailVerified, boolean isVerified) {
        UserStatus status = new UserStatus(userId, username, email, enabled, emailVerified, isVerified);
        userCache.put(userId, status);
        log.info("User cache updated for userId: {}", userId);
    }
    
    /**
     * Elimina usuario del cache (cuando se recibe evento de eliminación)
     */
    public void removeFromCache(UUID userId) {
        userCache.remove(userId);
        log.info("User removed from cache: {}", userId);
    }
}
