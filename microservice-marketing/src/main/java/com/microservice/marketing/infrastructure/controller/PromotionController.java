package com.microservice.marketing.infrastructure.controller;

import com.microservice.marketing.application.dto.PromotionDTO;
import com.microservice.marketing.application.usecase.ManagePromotionUseCase;
import com.microservice.marketing.domain.model.Promotion;
import com.microservice.marketing.infrastructure.adapter.mapper.DtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/promotions")
@Tag(name = "Promotions", description = "Operations related to promotions")
public class PromotionController {

    private final ManagePromotionUseCase managePromotionUseCase;
    private final DtoMapper dtoMapper;

    public PromotionController(ManagePromotionUseCase managePromotionUseCase, DtoMapper dtoMapper) {
        this.managePromotionUseCase = managePromotionUseCase;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new promotion")
    public ResponseEntity<PromotionDTO> createPromotion(@RequestBody Promotion promotion) {
        return ResponseEntity.ok(dtoMapper.toDto(managePromotionUseCase.createPromotion(promotion)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a promotion")
    public ResponseEntity<PromotionDTO> updatePromotion(@PathVariable UUID id, @RequestBody Promotion promotion) {
        return ResponseEntity.ok(dtoMapper.toDto(managePromotionUseCase.updatePromotion(id, promotion)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get promotion by ID")
    public ResponseEntity<PromotionDTO> getPromotion(@PathVariable UUID id) {
        return ResponseEntity.ok(dtoMapper.toDto(managePromotionUseCase.getPromotion(id)));
    }

    @GetMapping
    @Operation(summary = "Get all promotions")
    public ResponseEntity<List<PromotionDTO>> getAllPromotions() {
        return ResponseEntity.ok(managePromotionUseCase.getAllPromotions().stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete promotion by ID")
    public ResponseEntity<Void> deletePromotion(@PathVariable UUID id) {
        managePromotionUseCase.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }
}
