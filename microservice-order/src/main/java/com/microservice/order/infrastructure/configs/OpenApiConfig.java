package com.microservice.order.infrastructure.configs;

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
 * OpenAPI/Swagger configuration for Order Microservice API documentation.
 */
@Configuration
@SecurityScheme(
        name = "bearer-jwt",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT Bearer token authentication"
)
public class OpenApiConfig {

    @Value("${server.port:8084}")
    private int serverPort;

    @Bean
    public OpenAPI orderOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Microservice API")
                        .description("""
                                Order management microservice for the Karibea ecosystem.
                                
                                ## Architecture
                                - **Hexagonal Architecture** (Ports & Adapters)
                                - **Domain-Driven Design** (DDD)
                                - **Event-Driven** communication via Apache Kafka
                                
                                ## Features
                                - Create and manage orders
                                - Order lifecycle management (PENDING → CONFIRMED → SHIPPED → DELIVERED)
                                - Event publishing for inter-service communication
                                - Full audit trail via status history
                                
                                ## Authentication
                                All endpoints require JWT Bearer token authentication via OAuth2 Resource Server.
                                
                                ## Event-Driven Integration
                                This service publishes events to `order-events` topic and consumes from:
                                - `payment-events` - Payment confirmations, failures, refunds
                                - `shipment-events` - Shipping updates, delivery confirmations
                                - `inventory-events` - Stock reservation results
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Karibea Development Team")
                                .email("dev@karibea.com")
                                .url("https://github.com/AlvaroN-dev/Karibea-Backend"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local Development Server"),
                        new Server()
                                .url("http://gateway:8080/order-service")
                                .description("Gateway Route")
                ));
    }
}
