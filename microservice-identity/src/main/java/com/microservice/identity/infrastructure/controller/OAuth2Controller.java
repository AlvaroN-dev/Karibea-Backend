package com.microservice.identity.infrastructure.controller;

import com.microservice.identity.application.dto.response.AuthResponse;
import com.microservice.identity.application.mapper.UserMapper;
import com.microservice.identity.application.services.UserApplicationService;
import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.out.TokenServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for handling OAuth2 authentication callbacks.
 * Handles success and failure scenarios for social login (Google, GitHub,
 * Facebook, Microsoft).
 *
 * This controller works alongside AuthController:
 * - AuthController: Traditional login (email/password)
 * - OAuth2Controller: Social login (OAuth2 providers)
 *
 * Both generate the same JWT tokens for consistent authentication.
 *
 * Architecture:
 * - Infrastructure layer (adapter) - handles HTTP concerns
 * - Delegates to Application layer services for business logic
 * - Maintains hexagonal architecture principles
 */
@RestController
@RequestMapping("/api/v1/auth/oauth2")
@Tag(name = "OAuth2 Authentication", description = "Social login endpoints (Google, Microsoft, GitHub, Facebook)")
public class OAuth2Controller {

    private static final Logger log = LoggerFactory.getLogger(OAuth2Controller.class);

    // Constants
    private static final String BEARER_TOKEN_TYPE = "Bearer";
    private static final long EXPIRE_IN_SECONDS = 3600L;
    private static final String ATTRIBUTE_EMAIL = "email";
    private static final String ATTRIBUTE_NAME = "name";

    private final UserApplicationService userApplicationService;
    private final TokenServicePort tokenServicePort;

    public OAuth2Controller(UserApplicationService userApplicationService,
            TokenServicePort tokenServicePort) {
        this.userApplicationService = userApplicationService;
        this.tokenServicePort = tokenServicePort;
    }

    /**
     * Handles successful OAuth2 authentication.
     * Called after user successfully authenticates with OAuth2 provider (Google,
     * Microsoft, etc.).
     *
     * Flow:
     * 1. Extract user information from OAuth2 provider
     * 2. Find or create user in database
     * 3. Generate JWT tokens (same as traditional login)
     * 4. Return AuthResponse with tokens and user data
     *
     * @param principal OAuth2User containing user information from provider
     * @return Response with JWT tokens and user information
     */
    @Operation(summary = "OAuth2 login success callback", description = "Handles successful OAuth2 authentication. Creates or updates user and returns JWT tokens.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OAuth2 authentication successful - returns JWT tokens"),
            @ApiResponse(responseCode = "400", description = "Invalid OAuth2 user data"),
            @ApiResponse(responseCode = "500", description = "Error processing OAuth2 authentication")
    })
    @GetMapping("/success")
    public ResponseEntity<AuthResponse> handleOAuth2Success(
            @AuthenticationPrincipal OAuth2User principal) {

        if (principal == null) {
            log.error("OAuth2 success callback called with null principal");
            return ResponseEntity.badRequest().build();
        }

        try {
            return processOAuth2User(principal);
        } catch (Exception e) {
            log.error("Error processing OAuth2 success callback: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private ResponseEntity<AuthResponse> processOAuth2User(OAuth2User principal) {
        // Extract user information from OAuth2 provider
        String email = (String) principal.getAttribute(ATTRIBUTE_EMAIL);
        String name = (String) principal.getAttribute(ATTRIBUTE_NAME);
        String provider = determineProvider(principal);

        log.info("OAuth2 authentication successful - Provider: {}, Email: {}, Name: {}", provider, email, name);

        if (email == null) {
            log.warn("OAuth2 provider {} did not return an email address", provider);
            return ResponseEntity.badRequest().build();
        }

        // Find existing user
        User user = findUser(email);

        if (user == null) {
            // TDO: Implement user creation for OAuth2. For now, deny access if not
            // registered.
            log.warn("User not found for OAuth2 login: {}", email);
            return ResponseEntity.status(401).build(); // Unauthorized if user doesn't exist
        }

        // Generate JWT tokens
        String accessToken = tokenServicePort.generateToken(user);
        String refreshToken = tokenServicePort.generateRefreshToken(user);

        // Build response
        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setTokenType(BEARER_TOKEN_TYPE);
        response.setExpiresIn(EXPIRE_IN_SECONDS);
        response.setUser(UserMapper.toResponse(user));

        log.info("OAuth2 login successful, JWT generated for user: {}", email);
        return ResponseEntity.ok(response);
    }

    private User findUser(String email) {
        try {
            return userApplicationService.getUserByEmail(email);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Handles failed OAuth2 authentication.
     * Called when OAuth2 authentication fails.
     *
     * @return Response with error information
     */
    @Operation(summary = "OAuth2 login failure callback", description = "Handles failed OAuth2 authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "OAuth2 authentication failed")
    })
    @GetMapping("/failure")
    public ResponseEntity<Map<String, Object>> handleOAuth2Failure() {
        log.warn("OAuth2 authentication failed");

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "OAuth2 authentication failed");
        response.put("error", "Authentication with OAuth2 provider failed");

        return ResponseEntity.status(401).body(response);
    }

    /**
     * Determines the OAuth2 provider from user attributes.
     *
     * @param principal OAuth2User
     * @return Provider name (google, github, facebook, microsoft)
     */
    private String determineProvider(OAuth2User principal) {
        // Google uses 'sub' as user identifier
        if (principal.getAttribute("sub") != null &&
                principal.getAttribute("email_verified") != null) {
            return "google";
        }
        // GitHub uses 'id' and 'login'
        if (principal.getAttribute("login") != null) {
            return "github";
        }
        // Facebook uses 'id' and 'picture'
        if (principal.getAttribute("id") != null &&
                principal.getAttribute("picture") != null) {
            return "facebook";
        }
        // Microsoft uses 'oid' (object id)
        if (principal.getAttribute("oid") != null) {
            return "microsoft";
        }
        return "unknown";
    }
}
