package com.microservice.user.infrastructure.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for the User microservice.
 */
@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "JWT token obtained from microservice-identity"
)
public class OpenApiConfig {

    @Value("${spring.application.name:microservice-user}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("User Microservice API")
                .version("1.0.0")
                .description("""
                    Microservice for managing user profiles, addresses, preferences, and catalogs.
                    
                    ## Features
                    - User profile management (CRUD)
                    - Multiple addresses per user
                    - User preferences (language, currency, notifications)
                    - Catalog data (genders, currencies, languages)
                    
                    ## Architecture
                    - Hexagonal Architecture (Ports & Adapters)
                    - Event-driven communication via Kafka
                    - OAuth2/JWT security
                    
                    ## Authentication
                    All endpoints except catalogs require a valid JWT token.
                    """)
                .contact(new Contact()
                    .name("Karibea Team")
                    .email("support@karibea.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("/")
                    .description("Current server"),
                new Server()
                    .url("http://localhost:8080")
                    .description("Local development")
            ));
    }
}
