package com.microservice.shipping.application.usecases;

import com.microservice.shipping.domain.models.*;
import com.microservice.shipping.domain.port.in.CreateShipmentPort;
import com.microservice.shipping.domain.port.out.ShipmentEventPublisherPort;
import com.microservice.shipping.domain.port.out.ShipmentRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use case for creating shipments.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateShipmentUseCase implements CreateShipmentPort {

    private final ShipmentRepositoryPort shipmentRepository;
    private final ShipmentEventPublisherPort eventPublisher;

    @Override
    @Transactional
    public Shipment execute(CreateShipmentCommand command) {
        log.info("Creating shipment for order: {}", command.orderId());

        // Build addresses
        Address originAddress = command.originStreet() != null
                ? Address.of(command.originStreet(), command.originCity(),
                        command.originState(), command.originZipCode(), command.originCountry())
                : null;

        Address destinationAddress = Address.of(
                command.destinationStreet(),
                command.destinationCity(),
                command.destinationState(),
                command.destinationZipCode(),
                command.destinationCountry());

        // Build shipment items
        List<ShipmentItem> items = command.items().stream()
                .map(this::toShipmentItem)
                .toList();

        // Build shipping cost
        Money shippingCost = command.shippingCost() != null
                ? Money.of(command.shippingCost())
                : Money.zero();

        // Create shipment aggregate
        Shipment shipment = Shipment.create(
                command.orderId(),
                command.storeId(),
                command.customerId(),
                command.carrierId(),
                command.shippingMethodId(),
                command.carrierCode(),
                command.carrierName(),
                command.shippingMethodName(),
                originAddress,
                destinationAddress,
                shippingCost,
                items);

        // Set notes
        if (command.notes() != null) {
            shipment.setNotes(command.notes());
        }

        // Persist
        Shipment savedShipment = shipmentRepository.save(shipment);

        // Publish domain events
        eventPublisher.publishAll(savedShipment.getDomainEvents());
        savedShipment.clearDomainEvents();

        log.info("Shipment created with tracking number: {}", savedShipment.getTrackingNumber());

        return savedShipment;
    }

    private ShipmentItem toShipmentItem(ShipmentItemCommand cmd) {
        return ShipmentItem.create(
                cmd.orderItemId(),
                cmd.productId(),
                cmd.productName(),
                cmd.sku(),
                cmd.quantity());
    }
}
