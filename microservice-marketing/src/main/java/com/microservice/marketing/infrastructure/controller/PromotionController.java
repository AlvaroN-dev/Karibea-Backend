package com.microservice.marketing.infrastructure.controller;

import com.microservice.marketing.application.usecase.ManagePromotionUseCase;
import com.microservice.marketing.domain.model.Promotion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final ManagePromotionUseCase managePromotionUseCase;

    public PromotionController(ManagePromotionUseCase managePromotionUseCase) {
        this.managePromotionUseCase = managePromotionUseCase;
    }

    @PostMapping
    public ResponseEntity<Promotion> createPromotion(@RequestBody Promotion promotion) {
        return ResponseEntity.ok(managePromotionUseCase.createPromotion(promotion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promotion> updatePromotion(@PathVariable Long id, @RequestBody Promotion promotion) {
        return ResponseEntity.ok(managePromotionUseCase.updatePromotion(id, promotion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotion(@PathVariable Long id) {
        return ResponseEntity.ok(managePromotionUseCase.getPromotion(id));
    }

    @GetMapping
    public ResponseEntity<List<Promotion>> getAllPromotions() {
        return ResponseEntity.ok(managePromotionUseCase.getAllPromotions());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        managePromotionUseCase.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }
}
