package com.microservice.user.infrastructure.adapters.external;

import com.microservice.user.domain.port.out.IdentityServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador para comunicación con microservice-identity vía REST
 */
@Component
public class IdentityServiceAdapter implements IdentityServicePort {
    
    private static final Logger log = LoggerFactory.getLogger(IdentityServiceAdapter.class);
    
    private final WebClient identityWebClient;
    
    public IdentityServiceAdapter(WebClient identityWebClient) {
        this.identityWebClient = identityWebClient;
    }
    
    @Override
    public boolean userExists(UUID userId) {
        try {
            return Boolean.TRUE.equals(
                identityWebClient.get()
                    .uri("/api/v1/users/{id}/exists", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block()
            );
        } catch (WebClientResponseException.NotFound e) {
            return false;
        } catch (Exception e) {
            log.error("Error checking user existence for userId: {}", userId, e);
            return false;
        }
    }
    
    @Override
    public boolean isUserEnabled(UUID userId) {
        return getUserStatus(userId)
            .map(UserStatus::enabled)
            .orElse(false);
    }
    
    @Override
    public Optional<UserStatus> getUserStatus(UUID userId) {
        try {
            IdentityUserResponse response = identityWebClient.get()
                .uri("/api/v1/users/{id}", userId)
                .retrieve()
                .bodyToMono(IdentityUserResponse.class)
                .block();
            
            if (response == null) {
                return Optional.empty();
            }
            
            return Optional.of(new UserStatus(
                response.id(),
                response.username(),
                response.email(),
                response.enabled(),
                response.emailVerified(),
                response.isVerified()
            ));
        } catch (WebClientResponseException.NotFound e) {
            log.warn("User not found in identity service: {}", userId);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error fetching user status for userId: {}", userId, e);
            return Optional.empty();
        }
    }
    
    /**
     * DTO interno para mapear respuesta de Identity
     */
    private record IdentityUserResponse(
        UUID id,
        String username,
        String email,
        boolean enabled,
        boolean emailVerified,
        boolean isVerified
    ) {}
}
