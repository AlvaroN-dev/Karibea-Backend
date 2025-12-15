package com.microservice.payment.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.payment.domain.exceptions.PaymentMethodNotFoundException;
import com.microservice.payment.domain.models.UserPaymentMethod;
import com.microservice.payment.domain.port.in.SaveUserPaymentMethodUseCase;
import com.microservice.payment.domain.port.out.PaymentMethodRepositoryPort;
import com.microservice.payment.domain.port.out.UserPaymentMethodRepositoryPort;

/**
 * Implementation of SaveUserPaymentMethodUseCase.
 * SRP: Only handles saving user payment methods.
 */
@Service
@Transactional
public class SaveUserPaymentMethodService implements SaveUserPaymentMethodUseCase {

    private final PaymentMethodRepositoryPort paymentMethodRepository;
    private final UserPaymentMethodRepositoryPort userPaymentMethodRepository;

    public SaveUserPaymentMethodService(
            PaymentMethodRepositoryPort paymentMethodRepository,
            UserPaymentMethodRepositoryPort userPaymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.userPaymentMethodRepository = userPaymentMethodRepository;
    }

    @Override
    public UserPaymentMethod execute(SaveUserPaymentMethodCommand command) {
        // Validate payment method exists
        paymentMethodRepository.findById(command.paymentMethodId())
                .orElseThrow(() -> new PaymentMethodNotFoundException(command.paymentMethodId()));

        // Create user payment method
        UserPaymentMethod userPaymentMethod = UserPaymentMethod.builder()
                .externalUserId(command.externalUserId())
                .paymentMethodId(command.paymentMethodId())
                .tokenizedData(command.tokenizedData())
                .alias(command.alias())
                .maskedCardNumber(command.maskedCardNumber())
                .cardBrand(command.cardBrand())
                .expiryMonth(command.expiryMonth())
                .expiryYear(command.expiryYear())
                .isDefault(false)
                .isActive(true)
                .build();

        // Handle default setting
        if (command.setAsDefault()) {
            userPaymentMethodRepository.clearDefaultForUser(command.externalUserId());
            userPaymentMethod.setAsDefault();
        }

        return userPaymentMethodRepository.save(userPaymentMethod);
    }
}
