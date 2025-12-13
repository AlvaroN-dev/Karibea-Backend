package com.microservice.shipping.application.mapper;

import com.microservice.shipping.application.dto.request.CreateShipmentRequest;
import com.microservice.shipping.application.dto.response.ShipmentResponse;
import com.microservice.shipping.domain.models.*;
import com.microservice.shipping.domain.port.in.CreateShipmentPort;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between domain models and DTOs.
 */
@Component
public class ShipmentMapper {

    public ShipmentResponse toResponse(Shipment shipment) {
        return ShipmentResponse.builder()
                .id(shipment.getId())
                .trackingNumber(shipment.getTrackingNumber())
                .orderId(shipment.getExternalOrderId())
                .storeId(shipment.getExternalStoreId())
                .customerId(shipment.getExternalCustomerId())
                .carrierId(shipment.getCarrierId())
                .carrierCode(shipment.getCarrierCode())
                .carrierName(shipment.getCarrierName())
                .shippingMethodName(shipment.getShippingMethodName())
                .status(shipment.getStatus())
                .originAddress(toAddressResponse(shipment.getOriginAddress()))
                .destinationAddress(toAddressResponse(shipment.getDestinationAddress()))
                .shippingCost(shipment.getShippingCost() != null ? shipment.getShippingCost().amount() : null)
                .weightKg(shipment.getWeightKg())
                .dimensions(shipment.getDimensions())
                .notes(shipment.getNotes())
                .estimatedDeliveryDate(shipment.getEstimatedDeliveryDate())
                .pickedUpAt(shipment.getPickedUpAt())
                .deliveredAt(shipment.getDeliveredAt())
                .cancelledAt(shipment.getCancelledAt())
                .createdAt(shipment.getCreatedAt())
                .updatedAt(shipment.getUpdatedAt())
                .items(shipment.getItems().stream().map(this::toItemResponse).toList())
                .trackingEvents(shipment.getTrackingEvents().stream().map(this::toEventResponse).toList())
                .build();
    }

    private ShipmentResponse.AddressResponse toAddressResponse(Address address) {
        if (address == null)
            return null;
        return ShipmentResponse.AddressResponse.builder()
                .street(address.street())
                .city(address.city())
                .state(address.state())
                .zipCode(address.zipCode())
                .country(address.country())
                .fullAddress(address.getFullAddress())
                .build();
    }

    private ShipmentResponse.ShipmentItemResponse toItemResponse(ShipmentItem item) {
        return ShipmentResponse.ShipmentItemResponse.builder()
                .id(item.getId())
                .productId(item.getExternalProductId())
                .productName(item.getProductName())
                .sku(item.getSku())
                .quantity(item.getQuantity())
                .build();
    }

    private ShipmentResponse.TrackingEventResponse toEventResponse(TrackingEvent event) {
        return ShipmentResponse.TrackingEventResponse.builder()
                .id(event.getId())
                .status(event.getStatus())
                .location(event.getLocation())
                .description(event.getDescription())
                .occurredAt(event.getOccurredAt())
                .createdAt(event.getCreatedAt())
                .build();
    }

    public CreateShipmentPort.CreateShipmentCommand toCommand(CreateShipmentRequest request) {
        return new CreateShipmentPort.CreateShipmentCommand(
                request.orderId(),
                request.storeId(),
                request.customerId(),
                request.carrierId(),
                request.shippingMethodId(),
                request.carrierCode(),
                request.carrierName(),
                request.shippingMethodName(),
                request.originAddress() != null ? request.originAddress().street() : null,
                request.originAddress() != null ? request.originAddress().city() : null,
                request.originAddress() != null ? request.originAddress().state() : null,
                request.originAddress() != null ? request.originAddress().zipCode() : null,
                request.originAddress() != null ? request.originAddress().country() : null,
                request.destinationAddress().street(),
                request.destinationAddress().city(),
                request.destinationAddress().state(),
                request.destinationAddress().zipCode(),
                request.destinationAddress().country(),
                request.shippingCost(),
                request.items().stream().map(this::toItemCommand).toList(),
                request.notes());
    }

    private CreateShipmentPort.ShipmentItemCommand toItemCommand(CreateShipmentRequest.ShipmentItemRequest item) {
        return new CreateShipmentPort.ShipmentItemCommand(
                item.orderItemId(),
                item.productId(),
                item.productName(),
                item.sku(),
                item.quantity());
    }
}
