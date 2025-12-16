package com.microservice.user.domain.port.in;

import com.microservice.user.domain.models.UserProfile;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Puerto entrante - Caso de uso para actualizar perfil de usuario
 */
public interface UpdateUserProfileUseCase {
    
    UserProfile execute(UpdateUserProfileCommand command);
    
    record UpdateUserProfileCommand(
        UUID profileId,
        String firstName,
        String lastName,
        String middleName,
        String secondLastname,
        String phone,
        UUID genderId,
        String avatarUrl,
        LocalDate dateOfBirth
    ) {
        public UpdateUserProfileCommand {
            if (profileId == null) {
                throw new IllegalArgumentException("Profile ID is required");
            }
        }
    }
}
