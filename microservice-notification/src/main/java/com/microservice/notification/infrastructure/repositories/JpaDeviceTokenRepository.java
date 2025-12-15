package com.microservice.notification.infrastructure.repositories;

import com.microservice.notification.infrastructure.entities.DeviceTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaDeviceTokenRepository extends JpaRepository<DeviceTokenEntity, UUID> {
    List<DeviceTokenEntity> findByExternalUserId(UUID externalUserId);

    Optional<DeviceTokenEntity> findByExternalUserIdAndDeviceToken(UUID externalUserId, String deviceToken);
}
