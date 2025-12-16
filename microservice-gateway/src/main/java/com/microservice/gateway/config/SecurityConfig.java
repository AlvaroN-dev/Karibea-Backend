package com.microservice.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Security Configuration for the API Gateway.
 * Configures OAuth2 Resource Server with JWT validation and CORS.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    // Public endpoints that don't require authentication
    private static final String[] PUBLIC_ENDPOINTS = {
        // Authentication endpoints
        "/api/auth/**",
        "/api/auth/login",
        "/api/auth/register",
        "/api/auth/refresh",
        "/oauth2/**",
        "/.well-known/**",
        
        // Health and actuator endpoints
        "/actuator/**",
        "/actuator/health",
        "/actuator/info",
        
        // Swagger/OpenAPI endpoints
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/*/v3/api-docs",
        "/*/swagger-ui/**",
        "/webjars/**",
        
        // Fallback endpoints
        "/fallback/**"
    };

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            // Disable CSRF for stateless API
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            
            // Configure CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Configure authorization rules
            .authorizeExchange(exchanges -> exchanges
                // Allow OPTIONS requests for CORS preflight
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                
                // Allow public endpoints
                .pathMatchers(PUBLIC_ENDPOINTS).permitAll()
                
                // Public product/catalog viewing
                .pathMatchers(HttpMethod.GET, "/api/products/**", "/api/catalog/**", "/api/categories/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/stores/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/search/**").permitAll()
                
                // All other requests require authentication
                .anyExchange().authenticated()
            )
            
            // Configure OAuth2 Resource Server with JWT
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> {})
            )
            
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // Configure as needed for production
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Total-Count"));
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
