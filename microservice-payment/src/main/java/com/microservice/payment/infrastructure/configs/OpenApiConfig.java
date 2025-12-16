package com.microservice.payment.infrastructure.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for Payment Service API documentation.
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri:http://localhost:8080/realms/karibea}")
    private String issuerUri;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        final String oauthSchemeName = "oauth2";

        return new OpenAPI()
                .info(new Info()
                        .title("Payment Service API")
                        .description("""
                                # Payment Service
                                
                                RESTful API for payment processing within the Karibea e-commerce platform.
                                
                                ## Features
                                - **Transaction Management**: Create and process payment transactions
                                - **Refund Processing**: Handle full and partial refunds
                                - **Payment Methods**: Manage system and user-saved payment methods
                                - **Event-Driven**: Publishes events via Kafka for cross-service communication
                                
                                ## Architecture
                                - Built with hexagonal architecture (ports and adapters)
                                - Domain-Driven Design (DDD) principles
                                - Event-driven communication via Kafka
                                - Stripe integration for payment processing
                                
                                ## Authentication
                                This API requires OAuth2 JWT authentication. Include a valid Bearer token in the Authorization header.
                                
                                ## Scopes Required
                                - `payment:read` - Read transactions and payment methods
                                - `payment:write` - Create transactions and save payment methods
                                - `payment:refund` - Process refunds
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Karibea Team")
                                .email("dev@karibea.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8085").description("Local development"),
                        new Server().url("http://gateway:8080/payment").description("Through API Gateway")))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName)
                        .addList(oauthSchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Bearer token authentication"))
                        .addSecuritySchemes(oauthSchemeName,
                                new SecurityScheme()
                                        .name(oauthSchemeName)
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .description("OAuth2 authentication")
                                        .flows(new OAuthFlows()
                                                .authorizationCode(new OAuthFlow()
                                                        .authorizationUrl(issuerUri + "/protocol/openid-connect/auth")
                                                        .tokenUrl(issuerUri + "/protocol/openid-connect/token")
                                                        .scopes(new Scopes()
                                                                .addString("payment:read", "Read payment data")
                                                                .addString("payment:write", "Create and modify payments")
                                                                .addString("payment:refund", "Process refunds"))))));
    }
}
