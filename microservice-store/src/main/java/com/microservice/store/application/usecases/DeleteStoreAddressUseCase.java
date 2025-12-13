package com.microservice.store.application.usecases;

import com.microservice.store.domain.service.StoreAddressService;

public class DeleteStoreAddressUseCase {

    private final StoreAddressService service;

    public DeleteStoreAddressUseCase(StoreAddressService service) {
        this.service = service;
    }

    public void execute(Long id) {
        service.delete(id);
    }
}
