package com.microservice.payment.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a refund.
 */
public record RefundRequest(

        @NotNull(message = "Transaction ID is required") UUID transactionId,

        @NotNull(message = "Amount is required") @DecimalMin(value = "0.01", message = "Refund amount must be greater than zero") BigDecimal amount,

        @NotBlank(message = "Currency is required") @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code") String currency,

        @NotBlank(message = "Reason is required") @Size(max = 500, message = "Reason must not exceed 500 characters") String reason

) {
}
