package com.microservice.shipping.domain.port.out;

import com.microservice.shipping.domain.models.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port for Shipment persistence.
 */
public interface ShipmentRepositoryPort {

    Shipment save(Shipment shipment);

    Optional<Shipment> findById(UUID id);

    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    Page<Shipment> findByOrderId(UUID orderId, Pageable pageable);

    Page<Shipment> findByCustomerId(UUID customerId, Pageable pageable);

    boolean existsByTrackingNumber(String trackingNumber);

    void deleteById(UUID id);
}
