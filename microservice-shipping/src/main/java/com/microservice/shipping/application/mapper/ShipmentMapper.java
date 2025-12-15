package com.microservice.shipping.application.mapper;

import com.microservice.shipping.application.dto.request.CreateShipmentRequest;
import com.microservice.shipping.application.dto.response.*;
import com.microservice.shipping.domain.models.*;
import com.microservice.shipping.domain.port.in.CreateShipmentPort;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper for converting between domain models and DTOs.
 * Handles transformation of Shipment aggregates to enriched response DTOs.
 */
@Component
public class ShipmentMapper {

    /**
     * Converts a Shipment domain model to a ShipmentResponse DTO.
     * External entity references are included as info objects with their IDs.
     */
    public ShipmentResponse toResponse(Shipment shipment) {
        return ShipmentResponse.builder()
                .id(shipment.getId())
                .trackingNumber(shipment.getTrackingNumber())
                .order(OrderInfoResponse.withId(shipment.getExternalOrderId()))
                .store(StoreInfoResponse.withId(shipment.getExternalStoreId()))
                .customer(CustomerInfoResponse.withId(shipment.getExternalCustomerId()))
                .carrier(buildCarrierInfo(shipment))
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
                .items(shipment.getItems().stream()
                        .map(this::toItemResponse)
                        .collect(Collectors.toList()))
                .trackingEvents(shipment.getTrackingEvents().stream()
                        .map(this::toEventResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private CarrierInfoResponse buildCarrierInfo(Shipment shipment) {
        CarrierInfoResponse carrier = CarrierInfoResponse.withId(shipment.getCarrierId());
        carrier.setName(shipment.getCarrierName());
        carrier.setCode(shipment.getCarrierCode());
        return carrier;
    }

    private ShipmentResponse.AddressResponse toAddressResponse(Address address) {
        if (address == null) {
            return null;
        }
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
                .product(ProductInfoResponse.withId(item.getExternalProductId()))
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

    /**
     * Converts a CreateShipmentRequest DTO to a CreateShipmentCommand.
     */
    public CreateShipmentPort.CreateShipmentCommand toCommand(CreateShipmentRequest request) {
        return new CreateShipmentPort.CreateShipmentCommand(
                request.getOrderId(),
                request.getStoreId(),
                request.getCustomerId(),
                request.getCarrierId(),
                request.getShippingMethodId(),
                request.getCarrierCode(),
                request.getCarrierName(),
                request.getShippingMethodName(),
                request.getOriginAddress() != null ? request.getOriginAddress().getStreet() : null,
                request.getOriginAddress() != null ? request.getOriginAddress().getCity() : null,
                request.getOriginAddress() != null ? request.getOriginAddress().getState() : null,
                request.getOriginAddress() != null ? request.getOriginAddress().getZipCode() : null,
                request.getOriginAddress() != null ? request.getOriginAddress().getCountry() : null,
                request.getDestinationAddress().getStreet(),
                request.getDestinationAddress().getCity(),
                request.getDestinationAddress().getState(),
                request.getDestinationAddress().getZipCode(),
                request.getDestinationAddress().getCountry(),
                request.getShippingCost(),
                request.getItems().stream()
                        .map(this::toItemCommand)
                        .collect(Collectors.toList()),
                request.getNotes());
    }

    private CreateShipmentPort.ShipmentItemCommand toItemCommand(CreateShipmentRequest.ShipmentItemRequest item) {
        return new CreateShipmentPort.ShipmentItemCommand(
                item.getOrderItemId(),
                item.getProductId(),
                item.getProductName(),
                item.getSku(),
                item.getQuantity());
    }
}
