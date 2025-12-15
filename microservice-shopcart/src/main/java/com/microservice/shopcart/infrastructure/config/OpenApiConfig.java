package com.microservice.shopcart.infrastructure.config;

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
 * OpenAPI/Swagger configuration for the Shopping Cart microservice.
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI shoppingCartOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Shopping Cart Microservice API")
                .description("RESTful API for managing shopping carts, items, and coupon operations. " +
                             "Built following Domain-Driven Design and Hexagonal Architecture principles.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Karibea Development Team")
                    .email("dev@karibea.com")
                    .url("https://github.com/karibea"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:" + serverPort)
                    .description("Local Development Server"),
                new Server()
                    .url("https://api.karibea.com")
                    .description("Production Server")
            ));
    }
}
