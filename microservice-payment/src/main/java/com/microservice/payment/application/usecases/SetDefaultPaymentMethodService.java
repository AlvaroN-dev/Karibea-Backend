package com.microservice.payment.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.payment.domain.exceptions.PaymentMethodNotFoundException;
import com.microservice.payment.domain.models.UserPaymentMethod;
import com.microservice.payment.domain.port.in.SetDefaultPaymentMethodUseCase;
import com.microservice.payment.domain.port.out.UserPaymentMethodRepositoryPort;

/**
 * Implementation of SetDefaultPaymentMethodUseCase.
 * SRP: Only handles setting default payment method.
 */
@Service
@Transactional
public class SetDefaultPaymentMethodService implements SetDefaultPaymentMethodUseCase {

    private final UserPaymentMethodRepositoryPort userPaymentMethodRepository;

    public SetDefaultPaymentMethodService(UserPaymentMethodRepositoryPort userPaymentMethodRepository) {
        this.userPaymentMethodRepository = userPaymentMethodRepository;
    }

    @Override
    public void execute(UUID userPaymentMethodId, UUID externalUserId) {
        UserPaymentMethod userPaymentMethod = userPaymentMethodRepository.findById(userPaymentMethodId)
                .orElseThrow(() -> new PaymentMethodNotFoundException(userPaymentMethodId));

        // Verify ownership
        if (!userPaymentMethod.getExternalUserId().equals(externalUserId)) {
            throw new IllegalArgumentException("User payment method does not belong to user");
        }

        // Clear existing defaults and set new one
        userPaymentMethodRepository.clearDefaultForUser(externalUserId);
        userPaymentMethod.setAsDefault();
        userPaymentMethodRepository.save(userPaymentMethod);
    }
}
