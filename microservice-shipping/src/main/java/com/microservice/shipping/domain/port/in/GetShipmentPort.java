package com.microservice.shipping.domain.port.in;

import com.microservice.shipping.domain.models.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Inbound port for querying shipments.
 */
public interface GetShipmentPort {

    Optional<Shipment> findById(UUID shipmentId);

    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    Page<Shipment> findByOrderId(UUID orderId, Pageable pageable);

    Page<Shipment> findByCustomerId(UUID customerId, Pageable pageable);
}
