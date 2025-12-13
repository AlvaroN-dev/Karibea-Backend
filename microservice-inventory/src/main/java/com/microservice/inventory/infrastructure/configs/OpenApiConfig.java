package com.microservice.inventory.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI inventoryOpenAPI() {
                return new OpenAPI()
                                .info(new Info().title("Inventory Microservice API")
                                                .description("API for managing stock, reservations, and warehouses.")
                                                .version("v1.0.0")
                                                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement()
                                                .addList("bearer-key"))
                                .components(new io.swagger.v3.oas.models.Components()
                                                .addSecuritySchemes("bearer-key",
                                                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                                                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")));
        }
}
