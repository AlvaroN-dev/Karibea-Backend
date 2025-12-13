package com.microservice.shipping.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Request DTO for creating a new shipment.
 */
@Builder
public record CreateShipmentRequest(
        @NotNull(message = "Order ID is required") UUID orderId,

        UUID storeId,
        UUID customerId,

        @NotNull(message = "Carrier ID is required") UUID carrierId,

        UUID shippingMethodId,
        String carrierCode,
        String carrierName,
        String shippingMethodName,

        @Valid AddressRequest originAddress,

        @Valid @NotNull(message = "Destination address is required") AddressRequest destinationAddress,

        BigDecimal shippingCost,

        @Valid @NotEmpty(message = "At least one item is required") List<ShipmentItemRequest> items,

        String notes) {
    @Builder
    public record AddressRequest(
            @NotBlank(message = "Street is required") String street,

            @NotBlank(message = "City is required") String city,

            String state,
            String zipCode,

            @NotBlank(message = "Country is required") String country) {
    }

    @Builder
    public record ShipmentItemRequest(
            UUID orderItemId,
            UUID productId,

            @NotBlank(message = "Product name is required") String productName,

            String sku,

            @jakarta.validation.constraints.Min(value = 1, message = "Quantity must be at least 1") int quantity) {
    }
}
