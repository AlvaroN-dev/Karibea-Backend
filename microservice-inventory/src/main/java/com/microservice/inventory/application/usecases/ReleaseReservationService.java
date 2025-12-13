package com.microservice.inventory.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.inventory.domain.exceptions.ReservationNotFoundException;
import com.microservice.inventory.domain.exceptions.StockNotFoundException;
import com.microservice.inventory.domain.models.Stock;
import com.microservice.inventory.domain.models.StockReservation;
import com.microservice.inventory.domain.port.in.ReleaseReservationUseCase;
import com.microservice.inventory.domain.port.out.EventPublisherPort;
import com.microservice.inventory.domain.port.out.StockRepositoryPort;
import com.microservice.inventory.domain.port.out.StockReservationRepositoryPort;

@Service
@Transactional
public class ReleaseReservationService implements ReleaseReservationUseCase {

    private final StockRepositoryPort stockRepository;
    private final StockReservationRepositoryPort reservationRepository;
    private final EventPublisherPort eventPublisher;

    public ReleaseReservationService(StockRepositoryPort stockRepository,
            StockReservationRepositoryPort reservationRepository,
            EventPublisherPort eventPublisher) {
        this.stockRepository = stockRepository;
        this.reservationRepository = reservationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(ReleaseReservationCommand command) {
        StockReservation reservation = reservationRepository.findById(command.reservationId())
                .orElseThrow(() -> new ReservationNotFoundException(command.reservationId()));

        Stock stock = stockRepository.findById(reservation.getStockId())
                .orElseThrow(() -> new StockNotFoundException(reservation.getStockId()));

        stock.releaseReservation(reservation, command.reason());

        stockRepository.save(stock);
        reservationRepository.save(reservation);

        eventPublisher.publishAll(stock.getDomainEvents());
        stock.clearDomainEvents();
    }
}
