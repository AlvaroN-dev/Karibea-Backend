package com.microservice.shipping.application.usecases;

import com.microservice.shipping.application.exception.ShipmentNotFoundException;
import com.microservice.shipping.domain.models.Shipment;
import com.microservice.shipping.domain.port.in.CancelShipmentPort;
import com.microservice.shipping.domain.port.out.ShipmentEventPublisherPort;
import com.microservice.shipping.domain.port.out.ShipmentRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for cancelling shipments.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CancelShipmentUseCase implements CancelShipmentPort {

    private final ShipmentRepositoryPort shipmentRepository;
    private final ShipmentEventPublisherPort eventPublisher;

    @Override
    @Transactional
    public void execute(CancelShipmentCommand command) {
        log.info("Cancelling shipment: {}", command.shipmentId());

        Shipment shipment = shipmentRepository.findById(command.shipmentId())
                .orElseThrow(() -> ShipmentNotFoundException.withId(command.shipmentId()));

        shipment.cancel(command.reason(), command.cancelledBy());

        // Persist
        shipmentRepository.save(shipment);

        // Publish domain events
        eventPublisher.publishAll(shipment.getDomainEvents());
        shipment.clearDomainEvents();

        log.info("Shipment {} cancelled", command.shipmentId());
    }
}
