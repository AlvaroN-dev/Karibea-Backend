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

@RestController
@RequestMapping("/stores")
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
    public StoreResponseDto create(
            @Valid @RequestBody StoreCreateRequestDto request
    ) {
        Store store = StoreMapper.toDomain(request);
        return StoreMapper.toResponse(createUC.execute(store));
    }

    @GetMapping("/{id}")
    public StoreResponseDto getById(@PathVariable Long id) {
        return StoreMapper.toResponse(getUC.byId(id));
    }

    @GetMapping
    public List<StoreResponseDto> list() {
        return listUC.execute()
                .stream()
                .map(StoreMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public StoreResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody StoreUpdateRequestDto request
    ) {
        Store store = StoreMapper.toDomain(request);
        return StoreMapper.toResponse(updateUC.execute(id, store));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deleteUC.execute(id);
    }
}
