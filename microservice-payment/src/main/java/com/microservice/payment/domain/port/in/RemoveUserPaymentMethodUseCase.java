package com.microservice.payment.domain.port.in;

import java.util.UUID;

/**
 * Port IN - Remove user's saved payment method.
 * 
 * <p>
 * <b>SRP</b>: Only responsible for removing user payment methods.
 * </p>
 */
public interface RemoveUserPaymentMethodUseCase {

    /**
     * Removes a saved payment method for a user.
     * 
     * @param userPaymentMethodId the user payment method ID
     * @param externalUserId      the external user ID for verification
     */
    void execute(UUID userPaymentMethodId, UUID externalUserId);
}
