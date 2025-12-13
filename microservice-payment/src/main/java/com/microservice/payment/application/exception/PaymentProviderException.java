package com.microservice.payment.application.exception;

/**
 * Exception thrown when external payment provider communication fails.
 */
public class PaymentProviderException extends ApplicationException {

    public static final String CODE = "PAYMENT_PROVIDER_ERROR";

    private final String providerErrorCode;

    public PaymentProviderException(String message) {
        super(CODE, message);
        this.providerErrorCode = null;
    }

    public PaymentProviderException(String message, String providerErrorCode) {
        super(CODE, message);
        this.providerErrorCode = providerErrorCode;
    }

    public PaymentProviderException(String message, Throwable cause) {
        super(CODE, message, cause);
        this.providerErrorCode = null;
    }

    public String getProviderErrorCode() {
        return providerErrorCode;
    }
}
