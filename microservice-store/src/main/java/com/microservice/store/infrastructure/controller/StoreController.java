package com.microservice.store.infrastructure.controller;

import com.microservice.store.application.dto.*;
import com.microservice.store.application.mapper.StoreMapper;
import com.microservice.store.application.usecases.*;
import com.microservice.store.domain.models.Store;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@RestController
@RequestMapping("/stores")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Store", description = "Store Management APIs")
public class StoreController {

    private final CreateStoreUseCase createUC;
    private final GetStoreUseCase getUC;
    private final ListStoresUseCase listUC;
    private final UpdateStoreUseCase updateUC;
    private final DeleteStoreUseCase deleteUC;

    public StoreController(
            CreateStoreUseCase createUC,
            GetStoreUseCase getUC,
            ListStoresUseCase listUC,
            UpdateStoreUseCase updateUC,
            DeleteStoreUseCase deleteUC
    ) {
        this.createUC = createUC;
        this.getUC = getUC;
        this.listUC = listUC;
        this.updateUC = updateUC;
        this.deleteUC = deleteUC;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @io.swagger.v3.oas.annotations.Operation(summary = "Create a new store", description = "Creates a new store with initial information")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Store created successfully")
    public StoreResponseDto create(
            @Valid @RequestBody StoreCreateRequestDto request
    ) {
        Store store = StoreMapper.toDomain(request);
        return StoreMapper.toResponse(createUC.execute(store));
    }

    @GetMapping("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get store by ID", description = "Retrieves detailed information about a store")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Store found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Store not found")
    public StoreResponseDto getById(@PathVariable UUID id) {
        return StoreMapper.toResponse(getUC.byId(id));
    }

    @GetMapping
    @io.swagger.v3.oas.annotations.Operation(summary = "List all stores", description = "Retrieves a list of all registered stores")
    public List<StoreResponseDto> list() {
        return listUC.execute()
                .stream()
                .map(StoreMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Update store", description = "Updates an existing store information")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Store updated successfully")
    public StoreResponseDto update(
            @PathVariable UUID id,
            @Valid @RequestBody StoreUpdateRequestDto request
    ) {
        Store store = StoreMapper.toDomain(request);
        return StoreMapper.toResponse(updateUC.execute(id, store));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @io.swagger.v3.oas.annotations.Operation(summary = "Delete store", description = "Soft deletes a store")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Store deleted successfully")
    public void delete(@PathVariable UUID id) {
        deleteUC.execute(id);
    }
}
