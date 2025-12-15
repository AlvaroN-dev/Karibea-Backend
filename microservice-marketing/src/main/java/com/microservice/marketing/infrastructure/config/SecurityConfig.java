package com.microservice.marketing.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for microservices
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll() // Allow API and Swagger access
                        .anyRequest().authenticated());
        return http.build();
    }
}
