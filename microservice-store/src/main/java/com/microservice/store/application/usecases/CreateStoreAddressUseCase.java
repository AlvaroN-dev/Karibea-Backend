package com.microservice.store.application.usecases;

import com.microservice.store.domain.models.StoreAddress;
import com.microservice.store.domain.service.StoreAddressService;

public class CreateStoreAddressUseCase {

    private final StoreAddressService service;

    public CreateStoreAddressUseCase(StoreAddressService service) {
        this.service = service;
    }

    public StoreAddress execute(StoreAddress address) {
        return service.create(address);
    }
}

