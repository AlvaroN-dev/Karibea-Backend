package com.microservice.payment.infrastructure.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.payment.application.dto.PaymentMethodResponse;
import com.microservice.payment.application.dto.UserPaymentMethodRequest;
import com.microservice.payment.application.dto.UserPaymentMethodResponse;
import com.microservice.payment.application.mapper.PaymentMethodMapper;
import com.microservice.payment.domain.models.PaymentMethod;
import com.microservice.payment.domain.models.UserPaymentMethod;
import com.microservice.payment.domain.port.in.GetPaymentMethodsUseCase;
import com.microservice.payment.domain.port.in.GetUserPaymentMethodsUseCase;
import com.microservice.payment.domain.port.in.RemoveUserPaymentMethodUseCase;
import com.microservice.payment.domain.port.in.SaveUserPaymentMethodUseCase;
import com.microservice.payment.domain.port.in.SaveUserPaymentMethodUseCase.SaveUserPaymentMethodCommand;
import com.microservice.payment.domain.port.in.SetDefaultPaymentMethodUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST Controller for Payment Method operations.
 * Handles system payment methods and user-saved payment methods.
 */
@RestController
@RequestMapping("/api/v1/payment-methods")
@Validated
@Tag(name = "Payment Methods", description = "Payment methods management API")
public class PaymentMethodController {

    private final GetPaymentMethodsUseCase getPaymentMethodsUseCase;
    private final GetUserPaymentMethodsUseCase getUserPaymentMethodsUseCase;
    private final SaveUserPaymentMethodUseCase saveUserPaymentMethodUseCase;
    private final RemoveUserPaymentMethodUseCase removeUserPaymentMethodUseCase;
    private final SetDefaultPaymentMethodUseCase setDefaultPaymentMethodUseCase;
    private final PaymentMethodMapper paymentMethodMapper;

    public PaymentMethodController(
            GetPaymentMethodsUseCase getPaymentMethodsUseCase,
            GetUserPaymentMethodsUseCase getUserPaymentMethodsUseCase,
            SaveUserPaymentMethodUseCase saveUserPaymentMethodUseCase,
            RemoveUserPaymentMethodUseCase removeUserPaymentMethodUseCase,
            SetDefaultPaymentMethodUseCase setDefaultPaymentMethodUseCase,
            PaymentMethodMapper paymentMethodMapper) {
        this.getPaymentMethodsUseCase = getPaymentMethodsUseCase;
        this.getUserPaymentMethodsUseCase = getUserPaymentMethodsUseCase;
        this.saveUserPaymentMethodUseCase = saveUserPaymentMethodUseCase;
        this.removeUserPaymentMethodUseCase = removeUserPaymentMethodUseCase;
        this.setDefaultPaymentMethodUseCase = setDefaultPaymentMethodUseCase;
        this.paymentMethodMapper = paymentMethodMapper;
    }

    @Operation(
            summary = "Get all active payment methods",
            description = "Retrieves all system-level payment methods that are currently active and available for use")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment methods retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PaymentMethodResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - valid JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - payment:read scope required")
    })
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_payment:read')")
    public ResponseEntity<List<PaymentMethodResponse>> getActivePaymentMethods() {
        List<PaymentMethod> paymentMethods = getPaymentMethodsUseCase.getAll();
        return ResponseEntity.ok(paymentMethodMapper.toResponseList(paymentMethods));
    }

    @Operation(
            summary = "Get payment method by ID",
            description = "Retrieves a specific payment method by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment method found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentMethodResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - valid JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - payment:read scope required"),
            @ApiResponse(responseCode = "404", description = "Payment method not found")
    })
    @GetMapping("/{paymentMethodId}")
    @PreAuthorize("hasAuthority('SCOPE_payment:read')")
    public ResponseEntity<PaymentMethodResponse> getPaymentMethod(
            @Parameter(description = "Payment method unique identifier", required = true)
            @PathVariable UUID paymentMethodId) {

        PaymentMethod paymentMethod = getPaymentMethodsUseCase.getById(paymentMethodId);
        return ResponseEntity.ok(paymentMethodMapper.toResponse(paymentMethod));
    }

    @Operation(
            summary = "Get user's saved payment methods",
            description = "Retrieves all payment methods saved by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User payment methods retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserPaymentMethodResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - valid JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - payment:read scope required")
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_payment:read')")
    public ResponseEntity<List<UserPaymentMethodResponse>> getUserPaymentMethods(
            @Parameter(description = "User unique identifier", required = true)
            @PathVariable UUID userId) {

        List<UserPaymentMethod> userPaymentMethods = getUserPaymentMethodsUseCase.getByUserId(userId);

        return ResponseEntity.ok(
                userPaymentMethods.stream()
                        .map(upm -> {
                            PaymentMethod pm = getPaymentMethodsUseCase.getById(upm.getPaymentMethodId());
                            return paymentMethodMapper.toUserPaymentMethodResponse(upm, pm.getName());
                        })
                        .toList());
    }

    @Operation(
            summary = "Save a user payment method",
            description = "Saves a new payment method for the authenticated user with tokenized card data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment method saved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserPaymentMethodResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - valid JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - payment:write scope required"),
            @ApiResponse(responseCode = "404", description = "Payment method not found")
    })
    @PostMapping("/user")
    @PreAuthorize("hasAuthority('SCOPE_payment:write')")
    public ResponseEntity<UserPaymentMethodResponse> saveUserPaymentMethod(
            @Valid @RequestBody UserPaymentMethodRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());

        SaveUserPaymentMethodCommand command = new SaveUserPaymentMethodCommand(
                userId,
                request.getPaymentMethodId(),
                request.getTokenizedData(),
                request.getAlias(),
                request.getMaskedCardNumber(),
                request.getCardBrand(),
                request.getExpiryMonth(),
                request.getExpiryYear(),
                request.isSetAsDefault());

        UserPaymentMethod saved = saveUserPaymentMethodUseCase.execute(command);
        PaymentMethod pm = getPaymentMethodsUseCase.getById(saved.getPaymentMethodId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentMethodMapper.toUserPaymentMethodResponse(saved, pm.getName()));
    }

    @Operation(
            summary = "Remove a user payment method",
            description = "Removes a saved payment method for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Payment method removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - valid JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - payment:write scope required or user doesn't own this payment method"),
            @ApiResponse(responseCode = "404", description = "Payment method not found")
    })
    @DeleteMapping("/user/{userPaymentMethodId}")
    @PreAuthorize("hasAuthority('SCOPE_payment:write')")
    public ResponseEntity<Void> removeUserPaymentMethod(
            @Parameter(description = "User payment method unique identifier", required = true)
            @PathVariable UUID userPaymentMethodId,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());
        removeUserPaymentMethodUseCase.execute(userPaymentMethodId, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Set default payment method",
            description = "Sets a saved payment method as the default for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Default payment method set successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - valid JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - payment:write scope required or user doesn't own this payment method"),
            @ApiResponse(responseCode = "404", description = "Payment method not found")
    })
    @PutMapping("/user/{userPaymentMethodId}/default")
    @PreAuthorize("hasAuthority('SCOPE_payment:write')")
    public ResponseEntity<Void> setDefaultPaymentMethod(
            @Parameter(description = "User payment method unique identifier", required = true)
            @PathVariable UUID userPaymentMethodId,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());
        setDefaultPaymentMethodUseCase.execute(userPaymentMethodId, userId);
        return ResponseEntity.ok().build();
    }
}
