package com.microservice.payment.domain.port.in;

import java.util.UUID;

/**
 * Port IN - Set default payment method for user.
 * 
 * <p>
 * <b>SRP</b>: Only responsible for setting default payment method.
 * </p>
 */
public interface SetDefaultPaymentMethodUseCase {

    /**
     * Sets a payment method as default for a user.
     * 
     * @param userPaymentMethodId the user payment method ID
     * @param externalUserId      the external user ID for verification
     */
    void execute(UUID userPaymentMethodId, UUID externalUserId);
}
