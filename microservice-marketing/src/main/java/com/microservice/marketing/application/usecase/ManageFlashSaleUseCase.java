package com.microservice.marketing.application.usecase;

import com.microservice.marketing.domain.model.FlashSale;
import com.microservice.marketing.domain.port.FlashSaleRepositoryPort;
import org.springframework.stereotype.Service;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManageFlashSaleUseCase {

    private final FlashSaleRepositoryPort flashSaleRepository;

    public ManageFlashSaleUseCase(FlashSaleRepositoryPort flashSaleRepository) {
        this.flashSaleRepository = flashSaleRepository;
    }

    @Transactional
    public FlashSale createFlashSale(FlashSale flashSale) {
        return flashSaleRepository.save(flashSale);
    }

    @Transactional
    public FlashSale updateFlashSale(UUID id, FlashSale flashSale) {
        if (flashSaleRepository.findById(id).isEmpty()) {
            throw new RuntimeException("FlashSale not found with id: " + id);
        }
        flashSale.setId(id);
        return flashSaleRepository.save(flashSale);
    }

    @Transactional(readOnly = true)
    public FlashSale getFlashSale(UUID id) {
        return flashSaleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FlashSale not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<FlashSale> getAllFlashSales() {
        return flashSaleRepository.findAll();
    }

    @Transactional
    public void deleteFlashSale(UUID id) {
        flashSaleRepository.deleteById(id);
    }

    // Add method to add products logic if needed, but saving the FlashSale with
    // products list will handle it via cascade
}
