package com.microservice.user.domain.port.in;

import com.microservice.user.domain.models.UserProfile;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Puerto entrante - Caso de uso para crear perfil de usuario
 */
public interface CreateUserProfileUseCase {
    
    UserProfile execute(CreateUserProfileCommand command);
    
    record CreateUserProfileCommand(
        UUID externalUserId,
        String firstName,
        String lastName,
        String middleName,
        String secondLastname,
        String phone,
        UUID genderId,
        String avatarUrl,
        LocalDate dateOfBirth
    ) {
        public CreateUserProfileCommand {
            if (externalUserId == null) {
                throw new IllegalArgumentException("External user ID is required");
            }
            if (firstName == null || firstName.isBlank()) {
                throw new IllegalArgumentException("First name is required");
            }
            if (lastName == null || lastName.isBlank()) {
                throw new IllegalArgumentException("Last name is required");
            }
        }
    }
}
