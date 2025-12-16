package com.microservice.notification.infrastructure.adapters.mapper;

import com.microservice.notification.domain.model.DeviceToken;
import com.microservice.notification.infrastructure.entities.DeviceTokenEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between DeviceToken domain model and DeviceTokenEntity.
 */
@Component
public class DeviceTokenMapper {

    /**
     * Converts a DeviceTokenEntity to a DeviceToken domain model.
     *
     * @param entity the entity to convert
     * @return the domain model
     */
    public DeviceToken toDomain(DeviceTokenEntity entity) {
        if (entity == null) {
            return null;
        }

        DeviceToken deviceToken = new DeviceToken();
        deviceToken.setId(entity.getId());
        deviceToken.setExternalUserId(entity.getExternalUserId());
        deviceToken.setDeviceToken(entity.getDeviceToken());
        deviceToken.setPlatform(entity.getPlatform());
        deviceToken.setActive(entity.isActive());
        deviceToken.setLastUsedAt(entity.getLastUsedAt());
        deviceToken.setCreatedAt(entity.getCreatedAt());
        deviceToken.setUpdatedAt(entity.getUpdatedAt());

        return deviceToken;
    }

    /**
     * Converts a DeviceToken domain model to a DeviceTokenEntity.
     *
     * @param domain the domain model to convert
     * @return the entity
     */
    public DeviceTokenEntity toEntity(DeviceToken domain) {
        if (domain == null) {
            return null;
        }

        DeviceTokenEntity entity = new DeviceTokenEntity();
        entity.setId(domain.getId());
        entity.setExternalUserId(domain.getExternalUserId());
        entity.setDeviceToken(domain.getDeviceToken());
        entity.setPlatform(domain.getPlatform());
        entity.setActive(domain.isActive());
        entity.setLastUsedAt(domain.getLastUsedAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }
}
