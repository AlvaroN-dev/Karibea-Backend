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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST Controller for Transaction operations.
 * Handles payment transaction lifecycle: creation, processing, and status queries.
 */
@RestController
@RequestMapping("/api/v1/transactions")
@Validated
@Tag(name = "Transactions", description = "Payment transaction management API")
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

    @Operation(
            summary = "Create a new transaction",
            description = "Creates a new payment transaction in PENDING status. The transaction must be processed separately to complete the payment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - valid JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - payment:write scope required")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_payment:write')")
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {

        CreateTransactionCommand command = new CreateTransactionCommand(
                request.getExternalOrderId(),
                request.getExternalUserId(),
                request.getAmount(),
                request.getCurrency(),
                TransactionType.valueOf(request.getTransactionType()),
                request.getPaymentMethodId());

        Transaction transaction = createTransactionUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionMapper.toResponse(transaction));
    }

    @Operation(
            summary = "Process a pending transaction",
            description = "Processes a pending transaction with card payment details. This contacts the payment provider (Stripe) to complete the payment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction processed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or transaction not in PENDING status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - valid JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - payment:write scope required"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @PostMapping("/{transactionId}/process")
    @PreAuthorize("hasAuthority('SCOPE_payment:write')")
    public ResponseEntity<TransactionResponse> processTransaction(
            @Parameter(description = "Transaction unique identifier", required = true)
            @PathVariable UUID transactionId,
            @Valid @RequestBody ProcessTransactionRequest request) {

        ProcessTransactionCommand command = new ProcessTransactionCommand(
                transactionId,
                request.getCardToken(),
                request.getCvv());

        Transaction transaction = processTransactionUseCase.execute(command);
        return ResponseEntity.ok(transactionMapper.toResponse(transaction));
    }

    @Operation(
            summary = "Get transaction by ID",
            description = "Retrieves the current status and details of a specific transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - valid JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - payment:read scope required"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/{transactionId}")
    @PreAuthorize("hasAuthority('SCOPE_payment:read')")
    public ResponseEntity<TransactionResponse> getTransaction(
            @Parameter(description = "Transaction unique identifier", required = true)
            @PathVariable UUID transactionId) {

        Transaction transaction = getTransactionStatusUseCase.getById(transactionId);
        return ResponseEntity.ok(transactionMapper.toResponse(transaction));
    }

    @Operation(
            summary = "Get transactions by order ID",
            description = "Retrieves all transactions associated with a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - valid JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - payment:read scope required")
    })
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAuthority('SCOPE_payment:read')")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByOrder(
            @Parameter(description = "External order identifier", required = true)
            @PathVariable UUID orderId) {

        List<Transaction> transactions = getTransactionStatusUseCase.getByOrderId(orderId);
        return ResponseEntity.ok(transactionMapper.toResponseList(transactions));
    }

    @Operation(
            summary = "Get transactions by user ID",
            description = "Retrieves all transactions associated with a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - valid JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - payment:read scope required")
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_payment:read')")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByUser(
            @Parameter(description = "External user identifier", required = true)
            @PathVariable UUID userId) {

        List<Transaction> transactions = getTransactionStatusUseCase.getByUserId(userId);
        return ResponseEntity.ok(transactionMapper.toResponseList(transactions));
    }
}
