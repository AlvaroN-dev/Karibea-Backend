package com.microservice.store.application.usecases;

import com.microservice.store.domain.models.StoreAddress;
import com.microservice.store.domain.service.StoreAddressService;

import java.util.List;
import java.util.UUID;

public class GetStoreAddressesUseCase {

    private final StoreAddressService service;

    public GetStoreAddressesUseCase(StoreAddressService service) {
        this.service = service;
    }

    public List<StoreAddress> execute(UUID storeId) {
        return service.getAllByStoreId(storeId);
    }
}
