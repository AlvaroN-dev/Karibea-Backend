package com.microservice.gateway.config;

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
 * OpenAPI/Swagger configuration for the API Gateway.
 * Aggregates API documentation from all microservices.
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Karibea E-Commerce API Gateway")
                .version("1.0.0")
                .description("""
                    ## Karibea Backend API Gateway
                    
                    This is the central API Gateway for the Karibea E-Commerce platform. 
                    All requests to the microservices should be routed through this gateway.
                    
                    ### Available Services:
                    
                    - **Identity Service** - Authentication, OAuth2, JWT tokens
                    - **User Service** - User management and profiles
                    - **Catalog Service** - Product catalog and categories
                    - **Order Service** - Order management
                    - **Payment Service** - Payment processing
                    - **Inventory Service** - Stock management
                    - **Shopcart Service** - Shopping cart functionality
                    - **Marketing Service** - Promotions and coupons
                    - **Shipping Service** - Delivery management
                    - **Notification Service** - Email and push notifications
                    - **Review Service** - Product reviews
                    - **Search Service** - Full-text search
                    - **Store Service** - Store management
                    - **Chatbot Service** - AI-powered customer support
                    
                    ### Authentication:
                    
                    Most endpoints require a valid JWT token in the `Authorization` header:
                    ```
                    Authorization: Bearer <your-jwt-token>
                    ```
                    
                    ### Getting Started:
                    
                    1. Register a new user via `/api/auth/register`
                    2. Login to get a JWT token via `/api/auth/login`
                    3. Use the token to access protected endpoints
                    """)
                .contact(new Contact()
                    .name("Karibea Team")
                    .email("support@karibea.com")
                    .url("https://github.com/AlvaroN-dev/Karibea-Backend"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:" + serverPort)
                    .description("Local Development Server"),
                new Server()
                    .url("http://gateway:8080")
                    .description("Docker Environment")
            ));
    }
}
