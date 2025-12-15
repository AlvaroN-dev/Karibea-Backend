package com.microservice.payment.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.microservice.payment.application.dto.RefundResponse;
import com.microservice.payment.application.dto.TransactionResponse;
import com.microservice.payment.domain.models.Transaction;

/**
 * Mapper for converting between Transaction domain model and DTOs.
 * 
 * <p>
 * <b>Why mappers are needed:</b>
 * </p>
 * <ul>
 * <li>Separates domain from API concerns</li>
 * <li>Handles field name differences</li>
 * <li>Formats data for API consumption</li>
 * <li>Centralizes conversion logic</li>
 * </ul>
 */
@Component
public class TransactionMapper {

    private final RefundMapper refundMapper;

    public TransactionMapper(RefundMapper refundMapper) {
        this.refundMapper = refundMapper;
    }

    /**
     * Converts a Transaction domain model to TransactionResponse DTO.
     */
    public TransactionResponse toResponse(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        List<RefundResponse> refundResponses = transaction.getRefunds().stream()
                .map(refundMapper::toResponse)
                .collect(Collectors.toList());

        return new TransactionResponse(
                transaction.getId(),
                transaction.getExternalOrderId(),
                transaction.getExternalUserId(),
                transaction.getAmount().getAmount(),
                transaction.getAmount().getCurrency(),
                transaction.getStatus().name(),
                transaction.getStatus().getDescription(),
                transaction.getType().name(),
                transaction.getPaymentMethodId(),
                transaction.getProviderTransactionId(),
                transaction.getFailureReason(),
                refundResponses,
                transaction.calculateTotalRefunded().getAmount(),
                transaction.calculateRefundableAmount().getAmount(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt());
    }

    /**
     * Converts a list of Transaction domain models to TransactionResponse DTOs.
     */
    public List<TransactionResponse> toResponseList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
