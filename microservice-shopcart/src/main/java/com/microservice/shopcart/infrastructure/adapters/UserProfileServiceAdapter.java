package com.microservice.shopcart.infrastructure.adapters;

import com.microservice.shopcart.application.exception.ExternalServiceException;
import com.microservice.shopcart.domain.port.out.UserProfileServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.UUID;

/**
 * Adapter for communicating with User microservice via WebClient.
 */
@Component
public class UserProfileServiceAdapter implements UserProfileServicePort {

    private final WebClient webClient;
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    public UserProfileServiceAdapter(WebClient.Builder webClientBuilder,
                                      @Value("${services.user.url:http://localhost:8083}") String userUrl) {
        this.webClient = webClientBuilder
            .baseUrl(userUrl)
            .build();
    }

    @Override
    public UserProfileInfo getUserProfile(UUID userId) {
        try {
            UserServiceResponse response = webClient.get()
                .uri("/api/v1/users/{id}/profile", userId)
                .retrieve()
                .bodyToMono(UserServiceResponse.class)
                .timeout(TIMEOUT)
                .block();

            if (response == null) {
                return null;
            }

            return new UserProfileInfo(
                response.userId(),
                response.firstName(),
                response.lastName(),
                response.email(),
                response.phone()
            );
        } catch (Exception e) {
            // Return null if service is unavailable - cart can still function
            return null;
        }
    }

    private record UserServiceResponse(
        UUID userId,
        String firstName,
        String lastName,
        String email,
        String phone
    ) {}
}
