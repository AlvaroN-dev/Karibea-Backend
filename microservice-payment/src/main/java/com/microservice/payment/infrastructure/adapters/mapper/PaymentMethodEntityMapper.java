package com.microservice.payment.infrastructure.adapters.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.microservice.payment.domain.models.PaymentMethod;
import com.microservice.payment.domain.models.enums.PaymentMethodType;
import com.microservice.payment.domain.models.UserPaymentMethod;
import com.microservice.payment.infrastructure.entities.PaymentMethodEntity;
import com.microservice.payment.infrastructure.entities.PaymentMethodEntity.PaymentMethodTypeEnum;
import com.microservice.payment.infrastructure.entities.UserPaymentMethodEntity;

/**
 * Mapper for converting between PaymentMethod domain models and JPA entities.
 */
@Component
public class PaymentMethodEntityMapper {

    /**
     * Converts PaymentMethodEntity to domain PaymentMethod.
     */
    public PaymentMethod toDomain(PaymentMethodEntity entity) {
        if (entity == null) {
            return null;
        }

        return PaymentMethod.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(mapPaymentMethodType(entity.getType()))
                .providerCode(entity.getProviderCode())
                .isActive(entity.isActive())
                .description(entity.getDescription())
                .iconUrl(entity.getIconUrl())
                .displayOrder(entity.getDisplayOrder())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Converts domain PaymentMethod to PaymentMethodEntity.
     */
    public PaymentMethodEntity toEntity(PaymentMethod domain) {
        if (domain == null) {
            return null;
        }

        PaymentMethodEntity entity = new PaymentMethodEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setType(mapPaymentMethodTypeEnum(domain.getType()));
        entity.setProviderCode(domain.getProviderCode());
        entity.setActive(domain.isActive());
        entity.setDescription(domain.getDescription());
        entity.setIconUrl(domain.getIconUrl());
        entity.setDisplayOrder(domain.getDisplayOrder());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }

    /**
     * Converts UserPaymentMethodEntity to domain UserPaymentMethod.
     */
    public UserPaymentMethod toUserPaymentMethodDomain(UserPaymentMethodEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserPaymentMethod.builder()
                .id(entity.getId())
                .externalUserId(entity.getExternalUserId())
                .paymentMethodId(entity.getPaymentMethodId())
                .tokenizedData(entity.getTokenizedData())
                .alias(entity.getAlias())
                .maskedCardNumber(entity.getMaskedCardNumber())
                .cardBrand(entity.getCardBrand())
                .expiryMonth(entity.getExpiryMonth())
                .expiryYear(entity.getExpiryYear())
                .isDefault(entity.isDefault())
                .isActive(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Converts domain UserPaymentMethod to UserPaymentMethodEntity.
     */
    public UserPaymentMethodEntity toUserPaymentMethodEntity(UserPaymentMethod domain) {
        if (domain == null) {
            return null;
        }

        UserPaymentMethodEntity entity = new UserPaymentMethodEntity();
        entity.setId(domain.getId());
        entity.setExternalUserId(domain.getExternalUserId());
        entity.setPaymentMethodId(domain.getPaymentMethodId());
        entity.setTokenizedData(domain.getTokenizedData());
        entity.setAlias(domain.getAlias());
        entity.setMaskedCardNumber(domain.getMaskedCardNumber());
        entity.setCardBrand(domain.getCardBrand());
        entity.setExpiryMonth(domain.getExpiryMonth());
        entity.setExpiryYear(domain.getExpiryYear());
        entity.setDefault(domain.isDefault());
        entity.setActive(domain.isActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }

    public List<PaymentMethod> toDomainList(List<PaymentMethodEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public List<UserPaymentMethod> toUserPaymentMethodDomainList(List<UserPaymentMethodEntity> entities) {
        return entities.stream()
                .map(this::toUserPaymentMethodDomain)
                .collect(Collectors.toList());
    }

    private PaymentMethodType mapPaymentMethodType(PaymentMethodTypeEnum type) {
        return PaymentMethodType.valueOf(type.name());
    }

    private PaymentMethodTypeEnum mapPaymentMethodTypeEnum(PaymentMethodType type) {
        return PaymentMethodTypeEnum.valueOf(type.name());
    }
}
