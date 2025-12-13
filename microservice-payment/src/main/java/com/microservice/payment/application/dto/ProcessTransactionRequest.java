package com.microservice.payment.application.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for processing a pending transaction.
 */
public record ProcessTransactionRequest(

        @NotNull(message = "Transaction ID is required") UUID transactionId,

        @NotBlank(message = "Card token is required") String cardToken,

        String cvv

) {
}
