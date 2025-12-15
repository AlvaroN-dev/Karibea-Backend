package com.microservice.shipping.domain.port.out;

import com.microservice.shipping.domain.models.Carrier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port for Carrier persistence.
 */
public interface CarrierRepositoryPort {

    Carrier save(Carrier carrier);

    Optional<Carrier> findById(UUID id);

    Optional<Carrier> findByCode(String code);

    List<Carrier> findAllActive();

    boolean existsByCode(String code);
}
