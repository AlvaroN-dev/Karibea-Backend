package com.microservice.chatbot.infrastructure.config;

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
 * OpenAPI/Swagger configuration.
 * Location: infrastructure/config - Configuration class.
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8087}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Karibea Chatbot API")
                        .description("RAG (Retrieval-Augmented Generation) Chatbot API with Hexagonal Architecture")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Karibea Team")
                                .email("support@karibea.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort + "/api")
                                .description("Local Development Server")));
    }
}
