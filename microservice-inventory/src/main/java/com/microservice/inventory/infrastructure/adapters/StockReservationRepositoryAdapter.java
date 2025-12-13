package com.microservice.inventory.infrastructure.adapters;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.microservice.inventory.domain.models.StockReservation;
import com.microservice.inventory.domain.models.enums.ReservationStatus;
import com.microservice.inventory.domain.port.out.StockReservationRepositoryPort;
import com.microservice.inventory.infrastructure.adapters.mapper.ReservationEntityMapper;
import com.microservice.inventory.infrastructure.entities.StockReservationEntity;
import com.microservice.inventory.infrastructure.repositories.JpaStockReservationRepository;

@Component
public class StockReservationRepositoryAdapter implements StockReservationRepositoryPort {

    private final JpaStockReservationRepository jpaRepository;
    private final ReservationEntityMapper mapper;

    public StockReservationRepositoryAdapter(JpaStockReservationRepository jpaRepository,
            ReservationEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public StockReservation save(StockReservation reservation) {
        StockReservationEntity entity = mapper.toEntity(reservation);
        StockReservationEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<StockReservation> findById(UUID reservationId) {
        return jpaRepository.findById(reservationId).map(mapper::toDomain);
    }

    @Override
    public List<StockReservation> findByStockId(UUID stockId) {
        return jpaRepository.findByStockId(stockId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockReservation> findByExternalCartId(UUID externalCartId) {
        return jpaRepository.findByExternalCartId(externalCartId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockReservation> findByExternalOrderId(UUID externalOrderId) {
        return jpaRepository.findByExternalOrderId(externalOrderId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockReservation> findExpiredReservations(LocalDateTime now) {
        return jpaRepository.findExpiredReservations(now).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockReservation> findByStatus(ReservationStatus status) {
        return jpaRepository.findByStatus(status.name()).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
