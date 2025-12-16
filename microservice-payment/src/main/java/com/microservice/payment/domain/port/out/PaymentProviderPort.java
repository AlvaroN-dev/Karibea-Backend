package com.microservice.payment.domain.port.out;

import com.microservice.payment.domain.models.Money;
import com.microservice.payment.domain.models.Transaction;

/**
 * Port OUT - Contract for external payment provider integration.
 * 
 * <p>
 * <b>Why this port exists:</b>
 * </p>
 * <ul>
 * <li>Abstracts payment provider specifics (Stripe, PayPal, etc.)</li>
 * <li>Domain doesn't know about external APIs</li>
 * <li>Enables switching providers without domain changes</li>
 * <li>Facilitates testing with mock providers</li>
 * </ul>
 */
public interface PaymentProviderPort {

    /**
     * Processes a payment with the external provider.
     * 
     * @param request the payment request
     * @return the payment result
     */
    PaymentResult processPayment(PaymentRequest request);

    /**
     * Processes a refund with the external provider.
     * 
     * @param request the refund request
     * @return the refund result
     */
    RefundResult processRefund(RefundRequest request);

    /**
     * Validates a payment method token.
     * 
     * @param token the payment token
     * @return true if valid
     */
    boolean validateToken(String token);

    /**
     * Request object for payment processing.
     */
    record PaymentRequest(
            Transaction transaction,
            String cardToken,
            String cvv) {
    }

    /**
     * Result object for payment processing.
     */
    record PaymentResult(
            boolean success,
            String providerTransactionId,
            String errorCode,
            String errorMessage) {
        public static PaymentResult success(String providerTransactionId) {
            return new PaymentResult(true, providerTransactionId, null, null);
        }

        public static PaymentResult failure(String errorCode, String errorMessage) {
            return new PaymentResult(false, null, errorCode, errorMessage);
        }
    }

    /**
     * Request object for refund processing.
     */
    record RefundRequest(
            String providerTransactionId,
            Money amount,
            String reason) {
    }

    /**
     * Result object for refund processing.
     */
    record RefundResult(
            boolean success,
            String providerRefundId,
            String errorCode,
            String errorMessage) {
        public static RefundResult success(String providerRefundId) {
            return new RefundResult(true, providerRefundId, null, null);
        }

        public static RefundResult failure(String errorCode, String errorMessage) {
            return new RefundResult(false, null, errorCode, errorMessage);
        }
    }
}
