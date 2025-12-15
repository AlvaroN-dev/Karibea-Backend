package com.microservice.store.infrastructure.adapters;

import com.microservice.store.domain.port.UserGatewayPort;
import com.microservice.store.infrastructure.client.UserClient;
import feign.FeignException;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class UserAdapter implements UserGatewayPort {

    private final UserClient userClient;

    public UserAdapter(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public boolean exists(UUID userId) {
        try {
            userClient.getUser(userId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            // Log error, simplify assumption for now: if error, treat as not found or
            // throw?
            // Safer to throw default or return false.
            return false;
        }
    }
}
