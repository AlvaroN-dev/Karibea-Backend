package com.microservice.payment.infrastructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.payment.application.dto.RefundRequest;
import com.microservice.payment.application.dto.RefundResponse;
import com.microservice.payment.application.mapper.RefundMapper;
import com.microservice.payment.domain.models.Refund;
import com.microservice.payment.domain.port.in.RefundTransactionUseCase;
import com.microservice.payment.domain.port.in.RefundTransactionUseCase.RefundTransactionCommand;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST Controller for Refund operations.
 * Handles payment refund requests for completed transactions.
 */
@RestController
@RequestMapping("/api/v1/refunds")
@Validated
@Tag(name = "Refunds", description = "Payment refund management API")
public class RefundController {

    private final RefundTransactionUseCase refundTransactionUseCase;
    private final RefundMapper refundMapper;

    public RefundController(
            RefundTransactionUseCase refundTransactionUseCase,
            RefundMapper refundMapper) {
        this.refundTransactionUseCase = refundTransactionUseCase;
        this.refundMapper = refundMapper;
    }

    @Operation(
            summary = "Create a refund",
            description = "Creates a refund for a completed transaction. Partial refunds are supported. The refund is processed through the payment provider.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Refund created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RefundResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request - amount exceeds refundable amount or invalid transaction status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - valid JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - payment:refund scope required"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_payment:refund')")
    public ResponseEntity<RefundResponse> createRefund(
            @Valid @RequestBody RefundRequest request) {

        RefundTransactionCommand command = new RefundTransactionCommand(
                request.getTransactionId(),
                request.getAmount(),
                request.getCurrency(),
                request.getReason());

        Refund refund = refundTransactionUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refundMapper.toResponse(refund));
    }
}
