package com.microservice.payment.infrastructure.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.microservice.payment.domain.models.PaymentMethod;
import com.microservice.payment.domain.models.enums.PaymentMethodType;
import com.microservice.payment.domain.port.out.PaymentMethodRepositoryPort;
import com.microservice.payment.infrastructure.adapters.mapper.PaymentMethodEntityMapper;
import com.microservice.payment.infrastructure.entities.PaymentMethodEntity;
import com.microservice.payment.infrastructure.entities.PaymentMethodEntity.PaymentMethodTypeEnum;
import com.microservice.payment.infrastructure.repositories.JpaPaymentMethodRepository;

/**
 * Adapter implementing PaymentMethodRepositoryPort.
 */
@Component
public class PaymentMethodRepositoryAdapter implements PaymentMethodRepositoryPort {

    private final JpaPaymentMethodRepository jpaRepository;
    private final PaymentMethodEntityMapper mapper;

    public PaymentMethodRepositoryAdapter(
            JpaPaymentMethodRepository jpaRepository,
            PaymentMethodEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public PaymentMethod save(PaymentMethod paymentMethod) {
        PaymentMethodEntity entity = mapper.toEntity(paymentMethod);
        PaymentMethodEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<PaymentMethod> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<PaymentMethod> findAllActive() {
        List<PaymentMethodEntity> entities = jpaRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
        return mapper.toDomainList(entities);
    }

    @Override
    public List<PaymentMethod> findByType(PaymentMethodType type) {
        PaymentMethodTypeEnum typeEnum = PaymentMethodTypeEnum.valueOf(type.name());
        List<PaymentMethodEntity> entities = jpaRepository.findByType(typeEnum);
        return mapper.toDomainList(entities);
    }

    @Override
    public Optional<PaymentMethod> findByProviderCode(String providerCode) {
        return jpaRepository.findByProviderCode(providerCode)
                .map(mapper::toDomain);
    }
}
