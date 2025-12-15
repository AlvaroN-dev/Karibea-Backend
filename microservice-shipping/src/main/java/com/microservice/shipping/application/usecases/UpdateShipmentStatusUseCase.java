package com.microservice.shipping.application.usecases;

import com.microservice.shipping.application.exception.ShipmentNotFoundException;
import com.microservice.shipping.domain.models.Shipment;
import com.microservice.shipping.domain.models.ShipmentStatusEnum;
import com.microservice.shipping.domain.port.in.UpdateShipmentStatusPort;
import com.microservice.shipping.domain.port.out.ShipmentEventPublisherPort;
import com.microservice.shipping.domain.port.out.ShipmentRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for updating shipment status.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateShipmentStatusUseCase implements UpdateShipmentStatusPort {

    private final ShipmentRepositoryPort shipmentRepository;
    private final ShipmentEventPublisherPort eventPublisher;

    @Override
    @Transactional
    public void execute(UpdateStatusCommand command) {
        log.info("Updating shipment {} status to: {}", command.shipmentId(), command.newStatus());

        Shipment shipment = shipmentRepository.findById(command.shipmentId())
                .orElseThrow(() -> ShipmentNotFoundException.withId(command.shipmentId()));

        ShipmentStatusEnum newStatus = command.newStatus();

        // Apply status transition based on target status
        switch (newStatus) {
            case CONFIRMED -> shipment.confirm(command.updatedBy());
            case PICKED_UP -> shipment.pickUp(command.updatedBy());
            case IN_TRANSIT -> shipment.startTransit(command.location());
            case OUT_FOR_DELIVERY -> shipment.outForDelivery(command.location());
            case DELIVERED -> shipment.deliver(command.updatedBy(), command.location());
            case FAILED_DELIVERY -> shipment.failDelivery(command.reason(), command.location());
            case RETURNED -> shipment.initiateReturn(command.reason());
            default -> throw new IllegalArgumentException("Unsupported status transition: " + newStatus);
        }

        // Persist
        shipmentRepository.save(shipment);

        // Publish domain events
        eventPublisher.publishAll(shipment.getDomainEvents());
        shipment.clearDomainEvents();

        log.info("Shipment {} status updated to: {}", command.shipmentId(), newStatus);
    }
}
