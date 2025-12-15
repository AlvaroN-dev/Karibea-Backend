package com.microservice.payment.domain.port.in;

import java.util.List;
import java.util.UUID;

import com.microservice.payment.domain.models.UserPaymentMethod;

/**
 * Port IN - Query user's saved payment methods.
 * 
 * <p>
 * <b>SRP</b>: Only responsible for querying user payment methods.
 * </p>
 */
public interface GetUserPaymentMethodsUseCase {

    /**
     * Gets all saved payment methods for a user.
     * 
     * @param externalUserId the external user ID
     * @return list of user's saved payment methods
     */
    List<UserPaymentMethod> getByUserId(UUID externalUserId);

    /**
     * Gets the default payment method for a user.
     * 
     * @param externalUserId the external user ID
     * @return the default payment method or null
     */
    UserPaymentMethod getDefaultByUserId(UUID externalUserId);
}
