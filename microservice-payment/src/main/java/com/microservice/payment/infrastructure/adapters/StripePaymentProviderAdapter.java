package com.microservice.payment.infrastructure.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.payment.domain.port.out.PaymentProviderPort;

/**
 * Adapter implementing PaymentProviderPort for Stripe payment gateway.
 * 
 * <p>
 * <b>Why WebClient over RestTemplate:</b>
 * </p>
 * <ul>
 * <li>Non-blocking I/O for better resource utilization</li>
 * <li>Supports reactive programming model</li>
 * <li>RestTemplate is in maintenance mode</li>
 * <li>Better timeout and error handling</li>
 * </ul>
 * 
 * <p>
 * In production, this would use WebClient to call Stripe API.
 * </p>
 */
@Component
public class StripePaymentProviderAdapter implements PaymentProviderPort {

    private static final Logger log = LoggerFactory.getLogger(StripePaymentProviderAdapter.class);

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        log.info("Processing payment for transaction: {}", request.transaction().getId());

        // In production, this would call Stripe API via WebClient
        // For now, simulate successful payment
        try {
            // Simulate API call
            String providerTransactionId = "stripe_" + System.currentTimeMillis();

            log.info("Payment processed successfully: {}", providerTransactionId);
            return PaymentResult.success(providerTransactionId);

        } catch (Exception e) {
            log.error("Payment processing failed", e);
            return PaymentResult.failure("PAYMENT_FAILED", e.getMessage());
        }
    }

    @Override
    public RefundResult processRefund(RefundRequest request) {
        log.info("Processing refund for transaction: {}", request.providerTransactionId());

        try {
            // Simulate refund API call
            String providerRefundId = "refund_" + System.currentTimeMillis();

            log.info("Refund processed successfully: {}", providerRefundId);
            return RefundResult.success(providerRefundId);

        } catch (Exception e) {
            log.error("Refund processing failed", e);
            return RefundResult.failure("REFUND_FAILED", e.getMessage());
        }
    }

    @Override
    public boolean validateToken(String token) {
        // In production, validate token with Stripe
        return token != null && !token.isBlank();
    }
}
