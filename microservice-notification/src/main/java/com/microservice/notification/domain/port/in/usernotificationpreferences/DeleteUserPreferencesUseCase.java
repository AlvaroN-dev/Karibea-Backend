package com.microservice.notification.domain.port.in.usernotificationpreferences;

public interface DeleteUserPreferencesUseCase {
    void delete(String externalUserId);
}

