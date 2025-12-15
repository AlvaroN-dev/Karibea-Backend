package com.microservice.shipping.domain.port.in;

import com.microservice.shipping.domain.models.Shipment;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Inbound port for creating shipments.
 */
public interface CreateShipmentPort {

    Shipment execute(CreateShipmentCommand command);

    record CreateShipmentCommand(
            UUID orderId,
            UUID storeId,
            UUID customerId,
            UUID carrierId,
            UUID shippingMethodId,
            String carrierCode,
            String carrierName,
            String shippingMethodName,
            String originStreet,
            String originCity,
            String originState,
            String originZipCode,
            String originCountry,
            String destinationStreet,
            String destinationCity,
            String destinationState,
            String destinationZipCode,
            String destinationCountry,
            BigDecimal shippingCost,
            List<ShipmentItemCommand> items,
            String notes) {
    }

    record ShipmentItemCommand(
            UUID orderItemId,
            UUID productId,
            String productName,
            String sku,
            int quantity) {
    }
}
