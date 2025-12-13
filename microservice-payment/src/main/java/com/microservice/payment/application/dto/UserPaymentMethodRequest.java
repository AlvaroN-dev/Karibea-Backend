package com.microservice.payment.application.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for saving a user payment method.
 */
public record UserPaymentMethodRequest(

        @NotNull(message = "Payment method ID is required") UUID paymentMethodId,

        @NotBlank(message = "Tokenized data is required") String tokenizedData,

        String alias,

        String maskedCardNumber,

        String cardBrand,

        String expiryMonth,

        String expiryYear,

        boolean setAsDefault

) {
}
