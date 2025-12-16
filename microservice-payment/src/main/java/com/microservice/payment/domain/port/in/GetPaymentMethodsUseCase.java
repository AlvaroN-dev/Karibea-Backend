package com.microservice.payment.domain.port.in;

import java.util.List;
import java.util.UUID;

import com.microservice.payment.domain.models.PaymentMethod;

/**
 * Port IN - Query active payment methods from catalog.
 * 
 * <p>
 * <b>SRP</b>: Only responsible for querying system payment methods.
 * </p>
 */
public interface GetPaymentMethodsUseCase {

    /**
     * Gets all active payment methods.
     * 
     * @return list of active payment methods
     */
    List<PaymentMethod> getAll();

    /**
     * Gets a payment method by ID.
     * 
     * @param paymentMethodId the payment method ID
     * @return the payment method
     */
    PaymentMethod getById(UUID paymentMethodId);
}
