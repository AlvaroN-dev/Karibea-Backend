package com.microservice.payment.domain.port.in;

import java.util.UUID;

import com.microservice.payment.domain.models.UserPaymentMethod;

/**
 * Port IN - Save new payment method for user.
 * 
 * <p>
 * <b>SRP</b>: Only responsible for saving user payment methods.
 * </p>
 */
public interface SaveUserPaymentMethodUseCase {

    /**
     * Saves a new payment method for a user.
     * 
     * @param command the command containing payment method details
     * @return the saved user payment method
     */
    UserPaymentMethod execute(SaveUserPaymentMethodCommand command);

    /**
     * Command for saving a user payment method.
     */
    record SaveUserPaymentMethodCommand(
            UUID externalUserId,
            UUID paymentMethodId,
            String tokenizedData,
            String alias,
            String maskedCardNumber,
            String cardBrand,
            String expiryMonth,
            String expiryYear,
            boolean setAsDefault) {
        public SaveUserPaymentMethodCommand {
            if (externalUserId == null)
                throw new IllegalArgumentException("External User ID is required");
            if (paymentMethodId == null)
                throw new IllegalArgumentException("Payment Method ID is required");
            if (tokenizedData == null || tokenizedData.isBlank())
                throw new IllegalArgumentException("Tokenized data is required");
        }
    }
}
