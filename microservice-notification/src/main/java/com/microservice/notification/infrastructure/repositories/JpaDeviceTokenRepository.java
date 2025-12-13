package com.microservice.notification.infrastructure.repositories;

import com.microservice.notification.infrastructure.entities.DeviceTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaDeviceTokenRepository extends JpaRepository<DeviceTokenEntity, Long> {
    List<DeviceTokenEntity> findByExternalUserId(String externalUserId);

    Optional<DeviceTokenEntity> findByExternalUserIdAndDeviceToken(String externalUserId, String deviceToken);
}
