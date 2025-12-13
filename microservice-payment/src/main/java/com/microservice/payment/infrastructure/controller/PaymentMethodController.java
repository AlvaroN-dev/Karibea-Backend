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

import jakarta.validation.Valid;

/**
 * REST Controller for Payment Method operations.
 * 
 * <p>
 * <b>SOLID Compliance:</b>
 * </p>
 * <ul>
 * <li>Depends on 5 separate interfaces following SRP</li>
 * <li>Each use case has single responsibility</li>
 * <li>Controller only coordinates HTTP layer</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/payment-methods")
@Validated
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

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_payment:read')")
    public ResponseEntity<List<PaymentMethodResponse>> getActivePaymentMethods() {
        List<PaymentMethod> paymentMethods = getPaymentMethodsUseCase.getAll();
        return ResponseEntity.ok(paymentMethodMapper.toResponseList(paymentMethods));
    }

    @GetMapping("/{paymentMethodId}")
    @PreAuthorize("hasAuthority('SCOPE_payment:read')")
    public ResponseEntity<PaymentMethodResponse> getPaymentMethod(
            @PathVariable UUID paymentMethodId) {

        PaymentMethod paymentMethod = getPaymentMethodsUseCase.getById(paymentMethodId);
        return ResponseEntity.ok(paymentMethodMapper.toResponse(paymentMethod));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_payment:read')")
    public ResponseEntity<List<UserPaymentMethodResponse>> getUserPaymentMethods(
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

    @PostMapping("/user")
    @PreAuthorize("hasAuthority('SCOPE_payment:write')")
    public ResponseEntity<UserPaymentMethodResponse> saveUserPaymentMethod(
            @Valid @RequestBody UserPaymentMethodRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());

        SaveUserPaymentMethodCommand command = new SaveUserPaymentMethodCommand(
                userId,
                request.paymentMethodId(),
                request.tokenizedData(),
                request.alias(),
                request.maskedCardNumber(),
                request.cardBrand(),
                request.expiryMonth(),
                request.expiryYear(),
                request.setAsDefault());

        UserPaymentMethod saved = saveUserPaymentMethodUseCase.execute(command);
        PaymentMethod pm = getPaymentMethodsUseCase.getById(saved.getPaymentMethodId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentMethodMapper.toUserPaymentMethodResponse(saved, pm.getName()));
    }

    @DeleteMapping("/user/{userPaymentMethodId}")
    @PreAuthorize("hasAuthority('SCOPE_payment:write')")
    public ResponseEntity<Void> removeUserPaymentMethod(
            @PathVariable UUID userPaymentMethodId,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());
        removeUserPaymentMethodUseCase.execute(userPaymentMethodId, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/user/{userPaymentMethodId}/default")
    @PreAuthorize("hasAuthority('SCOPE_payment:write')")
    public ResponseEntity<Void> setDefaultPaymentMethod(
            @PathVariable UUID userPaymentMethodId,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());
        setDefaultPaymentMethodUseCase.execute(userPaymentMethodId, userId);
        return ResponseEntity.ok().build();
    }
}
