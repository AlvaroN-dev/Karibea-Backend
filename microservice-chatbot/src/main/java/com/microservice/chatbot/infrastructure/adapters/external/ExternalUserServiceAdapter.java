package com.microservice.chatbot.infrastructure.adapters.external;

import com.microservice.chatbot.domain.models.UserInfo;
import com.microservice.chatbot.domain.port.out.ExternalUserServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Adapter for retrieving user information from Identity microservice.
 * Location: infrastructure/adapters/external - Outbound adapter for external
 * service.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalUserServiceAdapter implements ExternalUserServicePort {

    private final WebClient identityWebClient;

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    @Override
    public Optional<UserInfo> getUserById(UUID userId) {
        if (userId == null) {
            return Optional.empty();
        }

        try {
            log.debug("Fetching user info for userId: {}", userId);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = identityWebClient
                    .get()
                    .uri("/api/users/{userId}", userId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(TIMEOUT)
                    .block();

            if (response == null) {
                log.warn("Empty response from identity service for userId: {}", userId);
                return Optional.empty();
            }

            UserInfo userInfo = UserInfo.builder()
                    .id(userId)
                    .username((String) response.get("username"))
                    .email((String) response.get("email"))
                    .build();

            log.debug("Successfully retrieved user info for userId: {}", userId);
            return Optional.of(userInfo);

        } catch (WebClientResponseException.NotFound e) {
            log.warn("User not found in identity service: {}", userId);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error fetching user info from identity service: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
