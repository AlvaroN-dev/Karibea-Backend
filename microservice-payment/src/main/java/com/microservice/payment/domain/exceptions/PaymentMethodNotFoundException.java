package com.microservice.payment.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a payment method is not found.
 */
public class PaymentMethodNotFoundException extends DomainException {

    public static final String CODE = "PAYMENT-005";

    private final UUID paymentMethodId;

    public PaymentMethodNotFoundException(UUID paymentMethodId) {
        super(CODE, String.format("Payment method not found with ID: %s", paymentMethodId));
        this.paymentMethodId = paymentMethodId;
    }

    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }
}
