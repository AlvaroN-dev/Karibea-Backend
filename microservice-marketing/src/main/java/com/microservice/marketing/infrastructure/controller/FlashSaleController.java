package com.microservice.marketing.infrastructure.controller;

import com.microservice.marketing.application.dto.FlashSaleDTO;
import com.microservice.marketing.application.usecase.ManageFlashSaleUseCase;
import com.microservice.marketing.domain.model.FlashSale;
import com.microservice.marketing.infrastructure.adapter.mapper.DtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flash-sales")
@Tag(name = "Flash Sales", description = "Operations related to flash sales")
public class FlashSaleController {

    private final ManageFlashSaleUseCase manageFlashSaleUseCase;
    private final DtoMapper dtoMapper;

    public FlashSaleController(ManageFlashSaleUseCase manageFlashSaleUseCase, DtoMapper dtoMapper) {
        this.manageFlashSaleUseCase = manageFlashSaleUseCase;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new flash sale")
    public ResponseEntity<FlashSaleDTO> createFlashSale(@RequestBody FlashSale flashSale) {
        return ResponseEntity.ok(dtoMapper.toDto(manageFlashSaleUseCase.createFlashSale(flashSale)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a flash sale")
    public ResponseEntity<FlashSaleDTO> updateFlashSale(@PathVariable UUID id, @RequestBody FlashSale flashSale) {
        return ResponseEntity.ok(dtoMapper.toDto(manageFlashSaleUseCase.updateFlashSale(id, flashSale)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get flash sale by ID")
    public ResponseEntity<FlashSaleDTO> getFlashSale(@PathVariable UUID id) {
        return ResponseEntity.ok(dtoMapper.toDto(manageFlashSaleUseCase.getFlashSale(id)));
    }

    @GetMapping
    @Operation(summary = "Get all flash sales")
    public ResponseEntity<List<FlashSaleDTO>> getAllFlashSales() {
        return ResponseEntity.ok(manageFlashSaleUseCase.getAllFlashSales().stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete flash sale by ID")
    public ResponseEntity<Void> deleteFlashSale(@PathVariable UUID id) {
        manageFlashSaleUseCase.deleteFlashSale(id);
        return ResponseEntity.noContent().build();
    }
}
