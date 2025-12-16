package com.microservice.payment.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.microservice.payment.application.dto.RefundResponse;
import com.microservice.payment.domain.models.Refund;

/**
 * Mapper for converting between Refund domain model and DTOs.
 */
@Component
public class RefundMapper {

    /**
     * Converts a Refund domain model to RefundResponse DTO.
     */
    public RefundResponse toResponse(Refund refund) {
        if (refund == null) {
            return null;
        }

        return new RefundResponse(
                refund.getId(),
                refund.getTransactionId(),
                refund.getAmount().getAmount(),
                refund.getAmount().getCurrency(),
                refund.getStatus().name(),
                refund.getStatus().getDescription(),
                refund.getReason(),
                refund.getProviderRefundId(),
                refund.getFailureReason(),
                refund.getCreatedAt(),
                refund.getProcessedAt());
    }

    /**
     * Converts a list of Refund domain models to RefundResponse DTOs.
     */
    public List<RefundResponse> toResponseList(List<Refund> refunds) {
        return refunds.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
