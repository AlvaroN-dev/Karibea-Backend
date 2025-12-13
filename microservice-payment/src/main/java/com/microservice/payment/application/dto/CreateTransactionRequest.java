package com.microservice.payment.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new transaction.
 * 
 * <p>
 * <b>Why DTOs separate from Domain:</b>
 * </p>
 * <ul>
 * <li>API contract stability - domain can evolve independently</li>
 * <li>Validation at API boundary</li>
 * <li>Prevent domain model exposure</li>
 * <li>Different serialization requirements</li>
 * </ul>
 */
public record CreateTransactionRequest(

        @NotNull(message = "External order ID is required") UUID externalOrderId,

        @NotNull(message = "External user ID is required") UUID externalUserId,

        @NotNull(message = "Amount is required") @DecimalMin(value = "0.01", message = "Amount must be greater than zero") BigDecimal amount,

        @NotBlank(message = "Currency is required") @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code") String currency,

        @NotBlank(message = "Transaction type is required") String transactionType,

        @NotNull(message = "Payment method ID is required") UUID paymentMethodId

) {
}
