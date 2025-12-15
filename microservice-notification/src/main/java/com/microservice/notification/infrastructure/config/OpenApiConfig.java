package com.microservice.notification.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:microservice-notification}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Notificaciones - Karibea")
                        .version("1.0.0")
                        .description("""
                                API RESTful para gestión de notificaciones del sistema Karibea.
                                
                                ## Características principales:
                                - **Notificaciones**: Crear, consultar y listar notificaciones por usuario
                                - **Device Tokens**: Gestión de tokens de dispositivos para push notifications
                                - **Preferencias**: Configuración de preferencias de notificación por usuario
                                
                                ## Canales soportados:
                                - EMAIL: Notificaciones por correo electrónico
                                - SMS: Notificaciones por mensaje de texto
                                - PUSH: Notificaciones push móviles
                                - IN_APP: Notificaciones dentro de la aplicación
                                
                                ## Identificadores:
                                Todos los IDs utilizan formato UUID para mayor seguridad y escalabilidad.
                                """)
                        .contact(new Contact()
                                .name("Equipo Karibea")
                                .email("soporte@karibea.com"))
                        .license(new License()
                                .name("Propietaria")
                                .url("https://karibea.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8085")
                                .description("Servidor de desarrollo local"),
                        new Server()
                                .url("http://api-gateway:8080")
                                .description("A través del API Gateway")));
    }
}
