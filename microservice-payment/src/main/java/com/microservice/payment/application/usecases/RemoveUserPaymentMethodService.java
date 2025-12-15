package com.microservice.payment.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.payment.domain.exceptions.PaymentMethodNotFoundException;
import com.microservice.payment.domain.models.UserPaymentMethod;
import com.microservice.payment.domain.port.in.RemoveUserPaymentMethodUseCase;
import com.microservice.payment.domain.port.out.UserPaymentMethodRepositoryPort;

/**
 * Implementation of RemoveUserPaymentMethodUseCase.
 * SRP: Only handles removing user payment methods.
 */
@Service
@Transactional
public class RemoveUserPaymentMethodService implements RemoveUserPaymentMethodUseCase {

    private final UserPaymentMethodRepositoryPort userPaymentMethodRepository;

    public RemoveUserPaymentMethodService(UserPaymentMethodRepositoryPort userPaymentMethodRepository) {
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

        userPaymentMethod.deactivate();
        userPaymentMethodRepository.save(userPaymentMethod);
    }
}
