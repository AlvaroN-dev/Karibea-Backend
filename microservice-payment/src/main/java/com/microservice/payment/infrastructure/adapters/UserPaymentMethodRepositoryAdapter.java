package com.microservice.payment.infrastructure.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.payment.domain.models.UserPaymentMethod;
import com.microservice.payment.domain.port.out.UserPaymentMethodRepositoryPort;
import com.microservice.payment.infrastructure.adapters.mapper.PaymentMethodEntityMapper;
import com.microservice.payment.infrastructure.entities.UserPaymentMethodEntity;
import com.microservice.payment.infrastructure.repositories.JpaUserPaymentMethodRepository;

/**
 * Adapter implementing UserPaymentMethodRepositoryPort.
 */
@Component
public class UserPaymentMethodRepositoryAdapter implements UserPaymentMethodRepositoryPort {

    private final JpaUserPaymentMethodRepository jpaRepository;
    private final PaymentMethodEntityMapper mapper;

    public UserPaymentMethodRepositoryAdapter(
            JpaUserPaymentMethodRepository jpaRepository,
            PaymentMethodEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public UserPaymentMethod save(UserPaymentMethod userPaymentMethod) {
        UserPaymentMethodEntity entity = mapper.toUserPaymentMethodEntity(userPaymentMethod);
        UserPaymentMethodEntity savedEntity = jpaRepository.save(entity);
        return mapper.toUserPaymentMethodDomain(savedEntity);
    }

    @Override
    public Optional<UserPaymentMethod> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toUserPaymentMethodDomain);
    }

    @Override
    public List<UserPaymentMethod> findByExternalUserId(UUID externalUserId) {
        List<UserPaymentMethodEntity> entities = jpaRepository
                .findByExternalUserIdAndIsActiveTrueOrderByIsDefaultDesc(externalUserId);
        return mapper.toUserPaymentMethodDomainList(entities);
    }

    @Override
    public Optional<UserPaymentMethod> findDefaultByExternalUserId(UUID externalUserId) {
        return jpaRepository.findByExternalUserIdAndIsDefaultTrue(externalUserId)
                .map(mapper::toUserPaymentMethodDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void clearDefaultForUser(UUID externalUserId) {
        jpaRepository.clearDefaultForUser(externalUserId);
    }
}
