package com.microservice.identity.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

/**
 * JWT configuration properties.
 * Binds JWT-related properties from application.yaml.
 * Provides centralized configuration for JWT token generation and validation.
 */

@Getter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String secret;
    private long expiration;
    private long refreshExpiration;

}
