package com.microservice.identity.infrastructure.controller;

import com.microservice.identity.application.dto.request.LoginRequest;
import com.microservice.identity.application.dto.response.AuthResponse;
import com.microservice.identity.application.dto.response.TokenResponse;
import com.microservice.identity.application.services.AuthApplicationService;
import com.microservice.identity.application.services.UserApplicationService;
import com.microservice.identity.application.mapper.UserMapper;
import com.microservice.identity.domain.exceptions.InvalidCredentialsException;
import com.microservice.identity.domain.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Authentication operations.
 * Handles user authentication, token management, and session control.
 * Follows hexagonal architecture and security best practices.
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication and authorization endpoints")
public class AuthController {

        private static final Logger log = LoggerFactory.getLogger(AuthController.class);

        // Constants
        private static final String BEARER_PREFIX = "Bearer ";
        private static final String TOKEN_TYPE_BEARER = "Bearer";
        private static final long ACCESS_TOKEN_EXPIRATION_SECONDS = 3600L; // 1 hour

        private final AuthApplicationService authApplicationService;
        private final UserApplicationService userApplicationService;

        public AuthController(AuthApplicationService authApplicationService,
                        UserApplicationService userApplicationService) {
                this.authApplicationService = authApplicationService;
                this.userApplicationService = userApplicationService;
        }

        @Operation(summary = "User login", description = "Authenticates a user with username and password. " +
                        "Returns JWT access token and refresh token upon successful authentication. " +
                        "Access token expires in 1 hour, refresh token in 24 hours.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Login successful - returns tokens and user data", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Invalid credentials - username or password incorrect"),
                        @ApiResponse(responseCode = "400", description = "Invalid input data - missing required fields"),
                        @ApiResponse(responseCode = "403", description = "Account disabled or not verified")
        })
        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Login credentials", required = true) @Valid @RequestBody LoginRequest request) {

                // Authenticate and generate access token
                String accessToken = authApplicationService.authenticateAndGenerateToken(
                                request.getUsername(),
                                request.getPassword());

                // Generate refresh token
                String refreshToken = authApplicationService.refreshToken(accessToken);

                // Get user data
                User user = userApplicationService.getUserByUsername(request.getUsername());

                // Build response
                AuthResponse response = new AuthResponse();
                response.setAccessToken(accessToken);
                response.setRefreshToken(refreshToken);
                response.setTokenType(TOKEN_TYPE_BEARER);
                response.setExpiresIn(ACCESS_TOKEN_EXPIRATION_SECONDS);
                response.setUser(UserMapper.toResponse(user));

                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Refresh access token", description = "Generates a new access token using a valid refresh token. "
                        +
                        "Use this endpoint when the access token expires to get a new one without re-authenticating.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Token refreshed successfully", content = @Content(schema = @Schema(implementation = TokenResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
        })
        @SecurityRequirement(name = "Bearer Authentication")
        @PostMapping("/refresh")
        public ResponseEntity<TokenResponse> refreshToken(
                        @Parameter(description = "Refresh token in Authorization header (Bearer token)", required = true, example = "Bearer eyJhbGciOiJIUzI1NiIs...") @RequestHeader("Authorization") String authorizationHeader) {

                String token = extractTokenFromHeader(authorizationHeader);
                String newAccessToken = authApplicationService.refreshToken(token);

                TokenResponse response = new TokenResponse();
                response.setToken(newAccessToken);
                response.setType(TOKEN_TYPE_BEARER);
                response.setExpiresIn(ACCESS_TOKEN_EXPIRATION_SECONDS);

                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Validate token", description = "Validates if an access token is valid and not expired. " +
                        "Returns 200 if valid, 401 if invalid or expired.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Token is valid and not expired"),
                        @ApiResponse(responseCode = "401", description = "Token is invalid, expired, or malformed")
        })
        @SecurityRequirement(name = "Bearer Authentication")
        @GetMapping("/validate")
        public ResponseEntity<Void> validateToken(
                        @Parameter(description = "Access token in Authorization header (Bearer token)", required = true) @RequestHeader("Authorization") String authorizationHeader) {

                return performTokenValidation(authorizationHeader);
        }

        /**
         * Performs token validation logic.
         * Extracted to a separate method to improve code organization and testability.
         * Follows SonarQube best practices by avoiding generic exception catching.
         * 
         * @param authorizationHeader the Authorization header containing the token
         * @return ResponseEntity with 200 if valid, 401 if invalid
         */
        private ResponseEntity<Void> performTokenValidation(String authorizationHeader) {
                String token = extractTokenFromHeader(authorizationHeader);

                try {
                        authApplicationService.validateToken(token);
                        return ResponseEntity.ok().build();
                } catch (InvalidCredentialsException e) {
                        log.warn("Token validation failed: Invalid credentials");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                } catch (RuntimeException e) {
                        log.error("Token validation failed: {}", e.getMessage());
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
        }

        @Operation(summary = "User logout", description = "Logs out a user by invalidating their token. " +
                        "Note: Current implementation is stateless, so token remains valid until expiration. " +
                        "For production, consider implementing token blacklist.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Logout successful"),
                        @ApiResponse(responseCode = "401", description = "Invalid token")
        })
        @SecurityRequirement(name = "Bearer Authentication")
        @PostMapping("/logout")
        public ResponseEntity<Void> logout(
                        @Parameter(description = "Access token in Authorization header (Bearer token)", required = true) @RequestHeader("Authorization") String authorizationHeader) {

                String token = extractTokenFromHeader(authorizationHeader);
                authApplicationService.logout(token);
                return ResponseEntity.ok().build();
        }

        /**
         * Extracts the JWT token from the Authorization header.
         * Removes the "Bearer " prefix if present.
         * 
         * This method follows the DRY (Don't Repeat Yourself) principle by centralizing
         * the token extraction logic that was previously duplicated across multiple
         * methods.
         * 
         * @param authorizationHeader the Authorization header value (e.g., "Bearer
         *                            eyJhbGciOiJIUzI1NiIs...")
         * @return the extracted JWT token without the "Bearer " prefix
         */
        private String extractTokenFromHeader(String authorizationHeader) {
                if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
                        return authorizationHeader.substring(BEARER_PREFIX.length());
                }
                return authorizationHeader;
        }
}
