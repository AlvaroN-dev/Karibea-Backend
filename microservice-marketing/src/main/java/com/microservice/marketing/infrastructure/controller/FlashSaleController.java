package com.microservice.marketing.infrastructure.controller;

import com.microservice.marketing.application.usecase.ManageFlashSaleUseCase;
import com.microservice.marketing.domain.model.FlashSale;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flash-sales")
public class FlashSaleController {

    private final ManageFlashSaleUseCase manageFlashSaleUseCase;

    public FlashSaleController(ManageFlashSaleUseCase manageFlashSaleUseCase) {
        this.manageFlashSaleUseCase = manageFlashSaleUseCase;
    }

    @PostMapping
    public ResponseEntity<FlashSale> createFlashSale(@RequestBody FlashSale flashSale) {
        return ResponseEntity.ok(manageFlashSaleUseCase.createFlashSale(flashSale));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlashSale> updateFlashSale(@PathVariable Long id, @RequestBody FlashSale flashSale) {
        return ResponseEntity.ok(manageFlashSaleUseCase.updateFlashSale(id, flashSale));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlashSale> getFlashSale(@PathVariable Long id) {
        return ResponseEntity.ok(manageFlashSaleUseCase.getFlashSale(id));
    }

    @GetMapping
    public ResponseEntity<List<FlashSale>> getAllFlashSales() {
        return ResponseEntity.ok(manageFlashSaleUseCase.getAllFlashSales());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashSale(@PathVariable Long id) {
        manageFlashSaleUseCase.deleteFlashSale(id);
        return ResponseEntity.noContent().build();
    }
}
