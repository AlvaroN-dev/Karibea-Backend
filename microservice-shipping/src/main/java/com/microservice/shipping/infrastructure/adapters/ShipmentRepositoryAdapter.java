package com.microservice.shipping.infrastructure.adapters;

import com.microservice.shipping.domain.models.*;
import com.microservice.shipping.domain.port.out.ShipmentRepositoryPort;
import com.microservice.shipping.infrastructure.entities.*;
import com.microservice.shipping.infrastructure.repositories.JpaShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter implementing ShipmentRepositoryPort using JPA.
 */
@Component
@RequiredArgsConstructor
public class ShipmentRepositoryAdapter implements ShipmentRepositoryPort {

    private final JpaShipmentRepository jpaRepository;

    @Override
    public Shipment save(Shipment shipment) {
        ShipmentEntity entity = toEntity(shipment);
        ShipmentEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Shipment> findById(UUID id) {
        return jpaRepository.findByIdWithDetails(id).map(this::toDomain);
    }

    @Override
    public Optional<Shipment> findByTrackingNumber(String trackingNumber) {
        return jpaRepository.findByTrackingNumber(trackingNumber).map(this::toDomain);
    }

    @Override
    public Page<Shipment> findByOrderId(UUID orderId, Pageable pageable) {
        return jpaRepository.findByExternalOrderId(orderId, pageable).map(this::toDomain);
    }

    @Override
    public Page<Shipment> findByCustomerId(UUID customerId, Pageable pageable) {
        return jpaRepository.findByExternalCustomerId(customerId, pageable).map(this::toDomain);
    }

    @Override
    public boolean existsByTrackingNumber(String trackingNumber) {
        return jpaRepository.existsByTrackingNumber(trackingNumber);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    // ========== Entity to Domain Mapping ==========

    private Shipment toDomain(ShipmentEntity entity) {
        return Shipment.builder()
                .id(entity.getId())
                .trackingNumber(entity.getTrackingNumber())
                .externalOrderId(entity.getExternalOrderId())
                .externalStoreId(entity.getExternalStoreId())
                .externalCustomerId(entity.getExternalCustomerId())
                .carrierId(entity.getCarrierId())
                .shippingMethodId(entity.getShippingMethodId())
                .carrierCode(entity.getCarrierCode())
                .carrierName(entity.getCarrierName())
                .shippingMethodName(entity.getShippingMethodName())
                .originAddress(toAddress(entity.getOriginStreet(), entity.getOriginCity(),
                        entity.getOriginState(), entity.getOriginZipCode(), entity.getOriginCountry()))
                .destinationAddress(toAddress(entity.getDestinationStreet(), entity.getDestinationCity(),
                        entity.getDestinationState(), entity.getDestinationZipCode(), entity.getDestinationCountry()))
                .status(ShipmentStatusEnum.valueOf(entity.getStatus()))
                .shippingCost(entity.getShippingCost() != null ? Money.of(entity.getShippingCost()) : Money.zero())
                .weightKg(entity.getWeightKg())
                .dimensions(entity.getDimensions())
                .notes(entity.getNotes())
                .estimatedDeliveryDate(entity.getEstimatedDeliveryDate())
                .pickedUpAt(entity.getPickedUpAt())
                .deliveredAt(entity.getDeliveredAt())
                .cancelledAt(entity.getCancelledAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .items(entity.getItems().stream().map(this::toItemDomain).collect(Collectors.toList()))
                .trackingEvents(
                        entity.getTrackingEvents().stream().map(this::toEventDomain).collect(Collectors.toList()))
                .build();
    }

    private Address toAddress(String street, String city, String state, String zipCode, String country) {
        if (street == null || city == null || country == null)
            return null;
        return Address.of(street, city, state, zipCode, country);
    }

    private ShipmentItem toItemDomain(ShipmentItemEntity entity) {
        return ShipmentItem.builder()
                .id(entity.getId())
                .shipmentId(entity.getShipment().getId())
                .externalOrderItemId(entity.getExternalOrderItemId())
                .externalProductId(entity.getExternalProductId())
                .productName(entity.getProductName())
                .sku(entity.getSku())
                .quantity(entity.getQuantity())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private TrackingEvent toEventDomain(TrackingEventEntity entity) {
        return TrackingEvent.builder()
                .id(entity.getId())
                .shipmentId(entity.getShipment().getId())
                .status(TrackingEventStatusEnum.valueOf(entity.getStatus()))
                .location(entity.getLocation())
                .description(entity.getDescription())
                .occurredAt(entity.getOccurredAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    // ========== Domain to Entity Mapping ==========

    private ShipmentEntity toEntity(Shipment shipment) {
        ShipmentEntity entity = ShipmentEntity.builder()
                .id(shipment.getId())
                .trackingNumber(shipment.getTrackingNumber())
                .externalOrderId(shipment.getExternalOrderId())
                .externalStoreId(shipment.getExternalStoreId())
                .externalCustomerId(shipment.getExternalCustomerId())
                .carrierId(shipment.getCarrierId())
                .shippingMethodId(shipment.getShippingMethodId())
                .carrierCode(shipment.getCarrierCode())
                .carrierName(shipment.getCarrierName())
                .shippingMethodName(shipment.getShippingMethodName())
                .originStreet(shipment.getOriginAddress() != null ? shipment.getOriginAddress().street() : null)
                .originCity(shipment.getOriginAddress() != null ? shipment.getOriginAddress().city() : null)
                .originState(shipment.getOriginAddress() != null ? shipment.getOriginAddress().state() : null)
                .originZipCode(shipment.getOriginAddress() != null ? shipment.getOriginAddress().zipCode() : null)
                .originCountry(shipment.getOriginAddress() != null ? shipment.getOriginAddress().country() : null)
                .destinationStreet(shipment.getDestinationAddress().street())
                .destinationCity(shipment.getDestinationAddress().city())
                .destinationState(shipment.getDestinationAddress().state())
                .destinationZipCode(shipment.getDestinationAddress().zipCode())
                .destinationCountry(shipment.getDestinationAddress().country())
                .status(shipment.getStatus().name())
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
                .build();

        // Map items
        shipment.getItems().forEach(item -> {
            ShipmentItemEntity itemEntity = toItemEntity(item, entity);
            entity.getItems().add(itemEntity);
        });

        // Map tracking events
        shipment.getTrackingEvents().forEach(event -> {
            TrackingEventEntity eventEntity = toEventEntity(event, entity);
            entity.getTrackingEvents().add(eventEntity);
        });

        return entity;
    }

    private ShipmentItemEntity toItemEntity(ShipmentItem item, ShipmentEntity shipment) {
        return ShipmentItemEntity.builder()
                .id(item.getId())
                .shipment(shipment)
                .externalOrderItemId(item.getExternalOrderItemId())
                .externalProductId(item.getExternalProductId())
                .productName(item.getProductName())
                .sku(item.getSku())
                .quantity(item.getQuantity())
                .createdAt(item.getCreatedAt())
                .build();
    }

    private TrackingEventEntity toEventEntity(TrackingEvent event, ShipmentEntity shipment) {
        return TrackingEventEntity.builder()
                .id(event.getId())
                .shipment(shipment)
                .status(event.getStatus().name())
                .location(event.getLocation())
                .description(event.getDescription())
                .occurredAt(event.getOccurredAt())
                .createdAt(event.getCreatedAt())
                .build();
    }
}
