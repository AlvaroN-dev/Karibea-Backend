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

        if (store.getStatus() != null) {
            dto.setStatus(toStatusDto(store.getStatus()));
        }

        if (store.getAddresses() != null) {
            dto.setAddresses(store.getAddresses().stream()
                    .map(StoreMapper::toAddressDto)
                    .collect(java.util.stream.Collectors.toList()));
        }

        if (store.getSettings() != null) {
            dto.setSettings(toSettingsDto(store.getSettings()));
        }

        return dto;
    }

    private static StoreStatusDto toStatusDto(com.microservice.store.domain.models.StoreStatus status) {
        StoreStatusDto dto = new StoreStatusDto();
        dto.setId(status.getId());
        dto.setName(status.getName());
        dto.setVerificationStatus(status.getVerificationStatus());
        dto.setDescription(status.getDescription());
        return dto;
    }

    private static StoreAddressDto toAddressDto(com.microservice.store.domain.models.StoreAddress address) {
        StoreAddressDto dto = new StoreAddressDto();
        dto.setId(address.getId());
        dto.setAddressType(address.getAddressType());
        dto.setStreetAddress(address.getStreetAddress());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());
        dto.setPrimary(address.isPrimary());
        return dto;
    }

    private static StoreSettingsDto toSettingsDto(com.microservice.store.domain.models.StoreSettings settings) {
        StoreSettingsDto dto = new StoreSettingsDto();
        dto.setId(settings.getId());
        dto.setReturnPolicy(settings.getReturnPolicy());
        dto.setShippingPolicy(settings.getShippingPolicy());
        dto.setMinOrderAmount(settings.getMinOrderAmount());
        dto.setAcceptsReturns(settings.isAcceptsReturns());
        dto.setReturnWindowDays(settings.getReturnWindowDays());
        return dto;
    }
}
