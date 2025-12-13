package com.microservice.store.application.mapper;

import com.microservice.store.application.dto.*;
import com.microservice.store.domain.models.Store;

public final class StoreMapper {

    private StoreMapper() {}

    /* REQUEST -> DOMAIN */

    public static Store toDomain(StoreCreateRequestDto dto) {
        Store store = new Store();
        store.setExternalOwnerUserId(dto.getExternalOwnerUserId());
        store.setName(dto.getName());
        store.setDescription(dto.getDescription());
        store.setEmail(dto.getEmail());
        store.setPhone(dto.getPhone());
        store.setLogoUrl(dto.getLogoUrl());
        store.setBannerUrl(dto.getBannerUrl());
        return store;
    }

    public static Store toDomain(StoreUpdateRequestDto dto) {
        Store store = new Store();
        store.setName(dto.getName());
        store.setDescription(dto.getDescription());
        store.setEmail(dto.getEmail());
        store.setPhone(dto.getPhone());
        store.setLogoUrl(dto.getLogoUrl());
        store.setBannerUrl(dto.getBannerUrl());
        return store;
    }

    /* DOMAIN -> RESPONSE */

    public static StoreResponseDto toResponse(Store store) {
        StoreResponseDto dto = new StoreResponseDto();
        dto.setId(store.getId());
        dto.setExternalOwnerUserId(store.getExternalOwnerUserId());
        dto.setName(store.getName());
        dto.setDescription(store.getDescription());
        dto.setEmail(store.getEmail());
        dto.setPhone(store.getPhone());
        dto.setLogoUrl(store.getLogoUrl());
        dto.setBannerUrl(store.getBannerUrl());
        return dto;
    }
}
