package com.microservice.identity.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * Security configuration for the application.
 * Configures JWT-based stateless authentication, OAuth2 social login, CORS, and
 * public endpoints.
 * Follows security best practices and hexagonal architecture principles.
 * 
 * Supports:
 * - JWT authentication for API endpoints
 * - OAuth2 social login (Google, GitHub, Facebook)
 * - CORS for cross-origin requests
 * - Public endpoints for authentication and documentation
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

        /**
         * Configures the security filter chain.
         * Supports both OAuth2 social login and JWT-based API authentication.
         * 
         * - Stateless session management for API endpoints (JWT-based)
         * - OAuth2 login for web-based authentication (Google, GitHub, Facebook)
         * - CORS enabled for cross-origin requests
         * - CSRF disabled for stateless API
         * - Public endpoints for authentication, OAuth2 callbacks, and Swagger
         * - All other endpoints require authentication
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // Disable CSRF for stateless API
                                .csrf(csrf -> csrf.disable())

                                // Enable CORS
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                                // Configure authorization
                                .authorizeHttpRequests(auth -> auth
                                                // Public endpoints - authentication
                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/api/v1/users/register").permitAll()
                                                .requestMatchers("/api/v1/users/verify-email").permitAll()

                                                // Public endpoints - OAuth2 login
                                                .requestMatchers("/oauth2/**").permitAll()
                                                .requestMatchers("/login/**").permitAll()
                                                .requestMatchers("/login/oauth2/**").permitAll()

                                                // Public endpoints - Swagger/OpenAPI
                                                .requestMatchers("/swagger-ui/**").permitAll()
                                                .requestMatchers("/v3/api-docs/**").permitAll()
                                                .requestMatchers("/swagger-ui.html").permitAll()

                                                // Public endpoints - actuator (if enabled)
                                                .requestMatchers("/actuator/health").permitAll()
                                                .requestMatchers("/actuator/info").permitAll()

                                                // All other endpoints require authentication
                                                .anyRequest().authenticated())

                                // OAuth2 Login Configuration
                                .oauth2Login(oauth2 -> oauth2
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/api/v1/auth/oauth2/success", true)
                                                .failureUrl("/api/v1/auth/oauth2/failure")
                                                .permitAll())

                                // Stateless session management for API endpoints (JWT)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                return http.build();
        }

        /**
         * Password encoder bean using BCrypt.
         * BCrypt is a strong hashing algorithm designed for passwords.
         * Automatically handles salting and is resistant to brute-force attacks.
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        /**
         * CORS configuration source.
         * Allows cross-origin requests from specified origins.
         * Configure allowed origins, methods, and headers.
         */
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                // Allowed origins (configure based on your frontend URL)
                configuration.setAllowedOrigins(List.of(
                                "http://localhost:3000", // React default
                                "http://localhost:4200", // Angular default
                                "http://localhost:8081", // Other frontend
                                "http://localhost:5173" // Vite default
                ));

                // Allowed HTTP methods
                configuration.setAllowedMethods(Arrays.asList(
                                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

                // Allowed headers
                configuration.setAllowedHeaders(Arrays.asList(
                                "Authorization",
                                "Content-Type",
                                "Accept",
                                "X-Requested-With"));

                // Expose headers
                configuration.setExposedHeaders(List.of("Authorization"));

                // Allow credentials (cookies, authorization headers)
                configuration.setAllowCredentials(true);

                // Max age for preflight requests (1 hour)
                configuration.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);

                return source;
        }
}
