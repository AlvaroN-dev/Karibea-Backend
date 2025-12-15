package com.microservice.shipping.infrastructure.adapters;

import com.microservice.shipping.domain.models.Carrier;
import com.microservice.shipping.domain.port.out.CarrierRepositoryPort;
import com.microservice.shipping.infrastructure.entities.CarrierEntity;
import com.microservice.shipping.infrastructure.repositories.JpaCarrierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter implementing CarrierRepositoryPort using JPA.
 */
@Component
@RequiredArgsConstructor
public class CarrierRepositoryAdapter implements CarrierRepositoryPort {

    private final JpaCarrierRepository jpaRepository;

    @Override
    public Carrier save(Carrier carrier) {
        CarrierEntity entity = toEntity(carrier);
        CarrierEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Carrier> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Carrier> findByCode(String code) {
        return jpaRepository.findByCode(code).map(this::toDomain);
    }

    @Override
    public List<Carrier> findAllActive() {
        return jpaRepository.findByIsActiveTrue().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }

    private Carrier toDomain(CarrierEntity entity) {
        return Carrier.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .trackingUrlTemplate(entity.getTrackingUrlTemplate())
                .isActive(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private CarrierEntity toEntity(Carrier carrier) {
        return CarrierEntity.builder()
                .id(carrier.getId())
                .code(carrier.getCode())
                .name(carrier.getName())
                .trackingUrlTemplate(carrier.getTrackingUrlTemplate())
                .isActive(carrier.isActive())
                .createdAt(carrier.getCreatedAt())
                .updatedAt(carrier.getUpdatedAt())
                .build();
    }
}
