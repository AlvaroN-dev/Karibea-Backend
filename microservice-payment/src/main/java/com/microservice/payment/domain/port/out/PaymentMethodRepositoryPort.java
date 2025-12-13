package com.microservice.payment.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.microservice.payment.domain.models.PaymentMethod;
import com.microservice.payment.domain.models.enums.PaymentMethodType;

/**
 * Port OUT - Repository contract for PaymentMethod persistence.
 */
public interface PaymentMethodRepositoryPort {

    /**
     * Saves a payment method (insert or update).
     * 
     * @param paymentMethod the payment method to save
     * @return the saved payment method
     */
    PaymentMethod save(PaymentMethod paymentMethod);

    /**
     * Finds a payment method by its ID.
     * 
     * @param id the payment method ID
     * @return optional containing the payment method if found
     */
    Optional<PaymentMethod> findById(UUID id);

    /**
     * Finds all active payment methods.
     * 
     * @return list of active payment methods
     */
    List<PaymentMethod> findAllActive();

    /**
     * Finds payment methods by type.
     * 
     * @param type the payment method type
     * @return list of payment methods of the given type
     */
    List<PaymentMethod> findByType(PaymentMethodType type);

    /**
     * Finds a payment method by provider code.
     * 
     * @param providerCode the provider code
     * @return optional containing the payment method if found
     */
    Optional<PaymentMethod> findByProviderCode(String providerCode);
}
