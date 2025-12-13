package com.microservice.payment.infrastructure.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.payment.application.dto.CreateTransactionRequest;
import com.microservice.payment.application.dto.ProcessTransactionRequest;
import com.microservice.payment.application.dto.TransactionResponse;
import com.microservice.payment.application.mapper.TransactionMapper;
import com.microservice.payment.domain.models.Transaction;
import com.microservice.payment.domain.models.enums.TransactionType;
import com.microservice.payment.domain.port.in.CreateTransactionUseCase;
import com.microservice.payment.domain.port.in.CreateTransactionUseCase.CreateTransactionCommand;
import com.microservice.payment.domain.port.in.GetTransactionStatusUseCase;
import com.microservice.payment.domain.port.in.ProcessTransactionUseCase;
import com.microservice.payment.domain.port.in.ProcessTransactionUseCase.ProcessTransactionCommand;

import jakarta.validation.Valid;

/**
 * REST Controller for Transaction operations.
 */
@RestController
@RequestMapping("/api/v1/transactions")
@Validated
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final ProcessTransactionUseCase processTransactionUseCase;
    private final GetTransactionStatusUseCase getTransactionStatusUseCase;
    private final TransactionMapper transactionMapper;

    public TransactionController(
            CreateTransactionUseCase createTransactionUseCase,
            ProcessTransactionUseCase processTransactionUseCase,
            GetTransactionStatusUseCase getTransactionStatusUseCase,
            TransactionMapper transactionMapper) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.processTransactionUseCase = processTransactionUseCase;
        this.getTransactionStatusUseCase = getTransactionStatusUseCase;
        this.transactionMapper = transactionMapper;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_payment:write')")
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {

        CreateTransactionCommand command = new CreateTransactionCommand(
                request.externalOrderId(),
                request.externalUserId(),
                request.amount(),
                request.currency(),
                TransactionType.valueOf(request.transactionType()),
                request.paymentMethodId());

        Transaction transaction = createTransactionUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionMapper.toResponse(transaction));
    }

    @PostMapping("/{transactionId}/process")
    @PreAuthorize("hasAuthority('SCOPE_payment:write')")
    public ResponseEntity<TransactionResponse> processTransaction(
            @PathVariable UUID transactionId,
            @Valid @RequestBody ProcessTransactionRequest request) {

        ProcessTransactionCommand command = new ProcessTransactionCommand(
                transactionId,
                request.cardToken(),
                request.cvv());

        Transaction transaction = processTransactionUseCase.execute(command);
        return ResponseEntity.ok(transactionMapper.toResponse(transaction));
    }

    @GetMapping("/{transactionId}")
    @PreAuthorize("hasAuthority('SCOPE_payment:read')")
    public ResponseEntity<TransactionResponse> getTransaction(
            @PathVariable UUID transactionId) {

        Transaction transaction = getTransactionStatusUseCase.getById(transactionId);
        return ResponseEntity.ok(transactionMapper.toResponse(transaction));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAuthority('SCOPE_payment:read')")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByOrder(
            @PathVariable UUID orderId) {

        List<Transaction> transactions = getTransactionStatusUseCase.getByOrderId(orderId);
        return ResponseEntity.ok(transactionMapper.toResponseList(transactions));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_payment:read')")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByUser(
            @PathVariable UUID userId) {

        List<Transaction> transactions = getTransactionStatusUseCase.getByUserId(userId);
        return ResponseEntity.ok(transactionMapper.toResponseList(transactions));
    }
}
