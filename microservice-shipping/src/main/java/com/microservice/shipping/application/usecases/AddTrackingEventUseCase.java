package com.microservice.shipping.application.usecases;

import com.microservice.shipping.application.exception.ShipmentNotFoundException;
import com.microservice.shipping.domain.models.Shipment;
import com.microservice.shipping.domain.models.TrackingEvent;
import com.microservice.shipping.domain.port.in.AddTrackingEventPort;
import com.microservice.shipping.domain.port.out.ShipmentEventPublisherPort;
import com.microservice.shipping.domain.port.out.ShipmentRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for adding tracking events.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AddTrackingEventUseCase implements AddTrackingEventPort {

    private final ShipmentRepositoryPort shipmentRepository;
    private final ShipmentEventPublisherPort eventPublisher;

    @Override
    @Transactional
    public void execute(AddTrackingEventCommand command) {
        log.info("Adding tracking event to shipment: {}", command.shipmentId());

        Shipment shipment = shipmentRepository.findById(command.shipmentId())
                .orElseThrow(() -> ShipmentNotFoundException.withId(command.shipmentId()));

        TrackingEvent trackingEvent = TrackingEvent.create(
                command.status(),
                command.location(),
                command.description(),
                command.occurredAt());

        shipment.addTrackingEvent(trackingEvent);

        // Persist
        shipmentRepository.save(shipment);

        // Publish domain events
        eventPublisher.publishAll(shipment.getDomainEvents());
        shipment.clearDomainEvents();

        log.info("Tracking event added to shipment: {}", command.shipmentId());
    }
}
