package com.microservice.order.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

/**
 * Request DTO for creating a new order.
 */
@Builder
public record CreateOrderRequest(
        @NotNull(message = "Customer ID is required") UUID customerId,

        @NotNull(message = "Store ID is required") UUID storeId,

        String currency,

        @Valid @NotNull(message = "Shipping address is required") AddressRequest shippingAddress,

        @Valid AddressRequest billingAddress,

        @Valid @NotEmpty(message = "At least one item is required") List<OrderItemRequest> items,

        String customerNotes) {
    @Builder
    public record AddressRequest(
            @NotBlank(message = "Street is required") String street,

            @NotBlank(message = "City is required") String city,

            String state,
            String zipCode,

            @NotBlank(message = "Country is required") String country) {
    }

    @Builder
    public record OrderItemRequest(
            @NotNull(message = "Product ID is required") UUID productId,

            @NotBlank(message = "Product name is required") String productName,

            String variantName,
            String sku,
            String imageUrl,

            @NotNull(message = "Unit price is required") java.math.BigDecimal unitPrice,

            @jakarta.validation.constraints.Min(value = 1, message = "Quantity must be at least 1") int quantity) {
    }
}
