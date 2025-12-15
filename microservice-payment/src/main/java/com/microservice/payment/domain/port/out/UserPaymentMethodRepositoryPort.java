package com.microservice.payment.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.microservice.payment.domain.models.UserPaymentMethod;

/**
 * Port OUT - Repository contract for UserPaymentMethod persistence.
 */
public interface UserPaymentMethodRepositoryPort {

    /**
     * Saves a user payment method (insert or update).
     * 
     * @param userPaymentMethod the user payment method to save
     * @return the saved user payment method
     */
    UserPaymentMethod save(UserPaymentMethod userPaymentMethod);

    /**
     * Finds a user payment method by its ID.
     * 
     * @param id the user payment method ID
     * @return optional containing the user payment method if found
     */
    Optional<UserPaymentMethod> findById(UUID id);

    /**
     * Finds all payment methods for a specific user.
     * 
     * @param externalUserId the external user ID
     * @return list of user payment methods
     */
    List<UserPaymentMethod> findByExternalUserId(UUID externalUserId);

    /**
     * Finds the default payment method for a user.
     * 
     * @param externalUserId the external user ID
     * @return optional containing the default payment method if exists
     */
    Optional<UserPaymentMethod> findDefaultByExternalUserId(UUID externalUserId);

    /**
     * Deletes a user payment method.
     * 
     * @param id the user payment method ID
     */
    void deleteById(UUID id);

    /**
     * Clears default flag for all user payment methods of a user.
     * 
     * @param externalUserId the external user ID
     */
    void clearDefaultForUser(UUID externalUserId);
}
