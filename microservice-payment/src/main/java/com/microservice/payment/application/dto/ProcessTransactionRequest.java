package com.microservice.payment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for processing a pending transaction.
 * Contains the payment credentials needed to complete the transaction.
 */
@Schema(description = "Request to process a pending payment transaction")
public class ProcessTransactionRequest {

    @Schema(description = "Transaction ID to process", 
            example = "d4e5f6a7-b8c9-0123-4567-89abcdef0123", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Transaction ID is required")
    private UUID transactionId;

    @Schema(description = "Tokenized card data from payment provider", 
            example = "tok_visa_4242424242424242", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Card token is required")
    private String cardToken;

    @Schema(description = "Card verification value (CVV/CVC)", 
            example = "123")
    private String cvv;

    // Constructors
    public ProcessTransactionRequest() {
    }

    public ProcessTransactionRequest(UUID transactionId, String cardToken, String cvv) {
        this.transactionId = transactionId;
        this.cardToken = cardToken;
        this.cvv = cvv;
    }

    // Getters and Setters
    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
