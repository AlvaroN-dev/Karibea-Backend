package com.microservice.payment.domain.models.enums;

/**
 * Value Object representing the type of payment method.
 * Helps categorize and apply specific validation rules per type.
 */
public enum PaymentMethodType {

    CREDIT_CARD("Credit Card", true, true),
    DEBIT_CARD("Debit Card", true, true),
    PAYPAL("PayPal", false, true),
    BANK_TRANSFER("Bank Transfer", false, false),
    DIGITAL_WALLET("Digital Wallet", false, true),
    CRYPTO("Cryptocurrency", false, false),
    CASH_ON_DELIVERY("Cash on Delivery", false, false);

    private final String displayName;
    private final boolean requiresCardDetails;
    private final boolean supportsRecurring;

    PaymentMethodType(String displayName, boolean requiresCardDetails, boolean supportsRecurring) {
        this.displayName = displayName;
        this.requiresCardDetails = requiresCardDetails;
        this.supportsRecurring = supportsRecurring;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Indicates if this payment type requires card number, CVV, etc.
     */
    public boolean requiresCardDetails() {
        return requiresCardDetails;
    }

    /**
     * Indicates if this payment type supports recurring payments.
     */
    public boolean supportsRecurring() {
        return supportsRecurring;
    }

    /**
     * Indicates if this payment type is processed immediately.
     */
    public boolean isInstant() {
        return this != BANK_TRANSFER && this != CASH_ON_DELIVERY;
    }

    /**
     * Indicates if refunds are supported for this payment type.
     */
    public boolean supportsRefund() {
        return this != CASH_ON_DELIVERY && this != CRYPTO;
    }
}
