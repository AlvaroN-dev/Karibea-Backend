package com.microservice.identity.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration.
 * Provides comprehensive API documentation with security schemes.
 * Accessible at /swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    private static final String BEARER_AUTH_SCHEME = "Bearer Authentication";
    /**
     * Configures OpenAPI documentation.
     * Includes API information, security schemes, and servers.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(apiServers())
                .addSecurityItem(securityRequirement())
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(BEARER_AUTH_SCHEME, securityScheme()));
    }

    /**
     * API information metadata.
     */
    private Info apiInfo() {
        return new Info()
                .title("Identity Microservice API")
                .description(
                        "RESTful API for user authentication and authorization. " +
                                "Provides endpoints for user management, role management, and JWT-based authentication. "
                                +
                                "Built with Spring Boot following hexagonal architecture and SOLID principles.")
                .version("1.0.0")
                .contact(apiContact())
                .license(apiLicense());
    }

    /**
     * Contact information.
     */
    private Contact apiContact() {
        return new Contact()
                .name("API Support")
                .email("support@example.com")
                .url("https://example.com/support");
    }

    /**
     * License information.
     */
    private License apiLicense() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    /**
     * API servers configuration.
     */
    private List<Server> apiServers() {
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local development server");

        Server devServer = new Server()
                .url("https://dev-api.example.com")
                .description("Development server");

        Server prodServer = new Server()
                .url("https://api.example.com")
                .description("Production server");

        return List.of(localServer, devServer, prodServer);
    }

    /**
     * Security requirement for JWT authentication.
     */
    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList(BEARER_AUTH_SCHEME);
    }

    /**
     * Security scheme for JWT Bearer token.
     */
    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name(BEARER_AUTH_SCHEME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Enter JWT token obtained from /api/v1/auth/login endpoint");
    }
}
