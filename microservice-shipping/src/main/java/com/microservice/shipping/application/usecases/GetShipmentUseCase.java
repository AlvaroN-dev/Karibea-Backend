package com.microservice.shipping.application.usecases;

import com.microservice.shipping.domain.models.Shipment;
import com.microservice.shipping.domain.port.in.GetShipmentPort;
import com.microservice.shipping.domain.port.out.ShipmentRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Use case for querying shipments.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class GetShipmentUseCase implements GetShipmentPort {

    private final ShipmentRepositoryPort shipmentRepository;

    @Override
    public Optional<Shipment> findById(UUID shipmentId) {
        log.debug("Finding shipment by id: {}", shipmentId);
        return shipmentRepository.findById(shipmentId);
    }

    @Override
    public Optional<Shipment> findByTrackingNumber(String trackingNumber) {
        log.debug("Finding shipment by tracking number: {}", trackingNumber);
        return shipmentRepository.findByTrackingNumber(trackingNumber);
    }

    @Override
    public Page<Shipment> findByOrderId(UUID orderId, Pageable pageable) {
        log.debug("Finding shipments for order: {}", orderId);
        return shipmentRepository.findByOrderId(orderId, pageable);
    }

    @Override
    public Page<Shipment> findByCustomerId(UUID customerId, Pageable pageable) {
        log.debug("Finding shipments for customer: {}", customerId);
        return shipmentRepository.findByCustomerId(customerId, pageable);
    }
}
