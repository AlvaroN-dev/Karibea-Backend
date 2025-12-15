package com.microservice.payment.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.microservice.payment.application.dto.PaymentMethodResponse;
import com.microservice.payment.application.dto.UserPaymentMethodResponse;
import com.microservice.payment.domain.models.PaymentMethod;
import com.microservice.payment.domain.models.UserPaymentMethod;

/**
 * Mapper for converting between PaymentMethod domain models and DTOs.
 */
@Component
public class PaymentMethodMapper {

    /**
     * Converts a PaymentMethod domain model to PaymentMethodResponse DTO.
     */
    public PaymentMethodResponse toResponse(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }

        return new PaymentMethodResponse(
                paymentMethod.getId(),
                paymentMethod.getName(),
                paymentMethod.getType().name(),
                paymentMethod.getType().getDisplayName(),
                paymentMethod.getProviderCode(),
                paymentMethod.isActive(),
                paymentMethod.getDescription(),
                paymentMethod.getIconUrl(),
                paymentMethod.getDisplayOrder(),
                paymentMethod.getType().requiresCardDetails(),
                paymentMethod.getType().supportsRecurring(),
                paymentMethod.getType().supportsRefund());
    }

    /**
     * Converts a list of PaymentMethod domain models to PaymentMethodResponse DTOs.
     */
    public List<PaymentMethodResponse> toResponseList(List<PaymentMethod> paymentMethods) {
        return paymentMethods.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts a UserPaymentMethod domain model to UserPaymentMethodResponse DTO.
     */
    public UserPaymentMethodResponse toUserPaymentMethodResponse(
            UserPaymentMethod userPaymentMethod,
            String paymentMethodName) {
        if (userPaymentMethod == null) {
            return null;
        }

        return new UserPaymentMethodResponse(
                userPaymentMethod.getId(),
                userPaymentMethod.getPaymentMethodId(),
                paymentMethodName,
                userPaymentMethod.getAlias(),
                userPaymentMethod.getMaskedCardNumber(),
                userPaymentMethod.getCardBrand(),
                userPaymentMethod.getExpiryMonth(),
                userPaymentMethod.getExpiryYear(),
                userPaymentMethod.isDefault(),
                userPaymentMethod.isActive(),
                userPaymentMethod.getCreatedAt());
    }
}
