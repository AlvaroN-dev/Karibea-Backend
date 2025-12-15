package com.microservice.inventory.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.inventory.domain.exceptions.StockNotFoundException;
import com.microservice.inventory.domain.models.Stock;
import com.microservice.inventory.domain.models.StockReservation;
import com.microservice.inventory.domain.port.in.ReserveStockUseCase;
import com.microservice.inventory.domain.port.out.EventPublisherPort;
import com.microservice.inventory.domain.port.out.StockRepositoryPort;
import com.microservice.inventory.domain.port.out.StockReservationRepositoryPort;

@Service
@Transactional
public class ReserveStockService implements ReserveStockUseCase {

    private final StockRepositoryPort stockRepository;
    private final StockReservationRepositoryPort reservationRepository;
    private final EventPublisherPort eventPublisher;

    public ReserveStockService(StockRepositoryPort stockRepository,
            StockReservationRepositoryPort reservationRepository,
            EventPublisherPort eventPublisher) {
        this.stockRepository = stockRepository;
        this.reservationRepository = reservationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public StockReservation execute(ReserveStockCommand command) {
        Stock stock = stockRepository.findById(command.stockId())
                .orElseThrow(() -> new StockNotFoundException(command.stockId()));

        StockReservation reservation = stock.reserveStock(
                command.quantity(),
                command.reservationType(),
                command.externalCartId(),
                command.externalOrderId(),
                command.expiresAt());

        stockRepository.save(stock);
        StockReservation saved = reservationRepository.save(reservation);

        eventPublisher.publishAll(stock.getDomainEvents());
        stock.clearDomainEvents();

        return saved;
    }
}
