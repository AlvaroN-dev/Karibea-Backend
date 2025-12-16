package com.microservice.payment.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.payment.domain.models.UserPaymentMethod;
import com.microservice.payment.domain.port.in.GetUserPaymentMethodsUseCase;
import com.microservice.payment.domain.port.out.UserPaymentMethodRepositoryPort;

/**
 * Implementation of GetUserPaymentMethodsUseCase.
 * SRP: Only handles user payment method queries.
 */
@Service
@Transactional(readOnly = true)
public class GetUserPaymentMethodsService implements GetUserPaymentMethodsUseCase {

    private final UserPaymentMethodRepositoryPort userPaymentMethodRepository;

    public GetUserPaymentMethodsService(UserPaymentMethodRepositoryPort userPaymentMethodRepository) {
        this.userPaymentMethodRepository = userPaymentMethodRepository;
    }

    @Override
    public List<UserPaymentMethod> getByUserId(UUID externalUserId) {
        return userPaymentMethodRepository.findByExternalUserId(externalUserId);
    }

    @Override
    public UserPaymentMethod getDefaultByUserId(UUID externalUserId) {
        return userPaymentMethodRepository.findDefaultByExternalUserId(externalUserId)
                .orElse(null);
    }
}
