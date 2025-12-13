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

import jakarta.validation.Valid;

/**
 * REST Controller for Refund operations.
 */
@RestController
@RequestMapping("/api/v1/refunds")
@Validated
public class RefundController {

    private final RefundTransactionUseCase refundTransactionUseCase;
    private final RefundMapper refundMapper;

    public RefundController(
            RefundTransactionUseCase refundTransactionUseCase,
            RefundMapper refundMapper) {
        this.refundTransactionUseCase = refundTransactionUseCase;
        this.refundMapper = refundMapper;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_payment:refund')")
    public ResponseEntity<RefundResponse> createRefund(
            @Valid @RequestBody RefundRequest request) {

        RefundTransactionCommand command = new RefundTransactionCommand(
                request.transactionId(),
                request.amount(),
                request.currency(),
                request.reason());

        Refund refund = refundTransactionUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refundMapper.toResponse(refund));
    }
}
