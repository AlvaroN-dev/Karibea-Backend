package com.microservice.payment.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.payment.domain.exceptions.PaymentMethodNotFoundException;
import com.microservice.payment.domain.models.PaymentMethod;
import com.microservice.payment.domain.port.in.GetPaymentMethodsUseCase;
import com.microservice.payment.domain.port.out.PaymentMethodRepositoryPort;

/**
 * Implementation of GetPaymentMethodsUseCase.
 * SRP: Only handles payment method queries.
 */
@Service
@Transactional(readOnly = true)
public class GetPaymentMethodsService implements GetPaymentMethodsUseCase {

    private final PaymentMethodRepositoryPort paymentMethodRepository;

    public GetPaymentMethodsService(PaymentMethodRepositoryPort paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public List<PaymentMethod> getAll() {
        return paymentMethodRepository.findAllActive();
    }

    @Override
    public PaymentMethod getById(UUID paymentMethodId) {
        return paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new PaymentMethodNotFoundException(paymentMethodId));
    }
}
