package com.microservice.shipping.infrastructure.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for the Shipping Microservice.
 * Provides comprehensive API documentation with security schemes.
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8086}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local Development Server"),
                        new Server()
                                .url("http://microservice-shipping:" + serverPort)
                                .description("Docker/Kubernetes Internal")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token obtained from the Identity service")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    private Info apiInfo() {
        return new Info()
                .title("Shipping Microservice API")
                .description("""
                        REST API for shipment management in the Karibea ecosystem.
                        
                        ## Features
                        - **Shipment Creation**: Create shipments from order confirmations
                        - **Tracking Management**: Real-time tracking with event history
                        - **Status Updates**: Comprehensive status workflow management
                        - **Carrier Integration**: Support for multiple carriers and methods
                        
                        ## Architecture
                        - **Hexagonal Architecture** with Ports & Adapters
                        - **Event-Driven** communication via Apache Kafka
                        - **Domain-Driven Design** principles
                        
                        ## Events Published
                        - `ShipmentCreated`, `ShipmentPickedUp`, `ShipmentDelivered`
                        - `ShipmentCancelled`, `ShipmentReturned`, `TrackingEventAdded`
                        
                        ## Events Consumed
                        - `OrderConfirmed` (from order-events topic)
                        - `OrderCancelled` (from order-events topic)
                        """)
                .version("1.0.0")
                .contact(new Contact()
                        .name("Karibea Development Team")
                        .email("dev@karibea.com")
                        .url("https://github.com/AlvaroN-dev/Karibea-Backend"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }
}
