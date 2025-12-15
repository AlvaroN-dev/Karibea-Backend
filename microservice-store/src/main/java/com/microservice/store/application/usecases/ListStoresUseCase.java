package com.microservice.store.application.usecases;

import com.microservice.store.domain.models.Store;
import com.microservice.store.domain.service.StoreService;

import java.util.List;

public class ListStoresUseCase {

    private final StoreService service;

    public ListStoresUseCase(StoreService service) {
        this.service = service;
    }

    public List<Store> execute() {
        return service.getAllActive();
    }
}
