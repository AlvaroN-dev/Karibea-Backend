package com.microservice.user.domain.service;

import com.microservice.user.domain.exceptions.UserProfileAlreadyExistsException;
import com.microservice.user.domain.models.Gender;
import com.microservice.user.domain.models.UserProfile;
import com.microservice.user.domain.port.out.GenderRepositoryPort;
import com.microservice.user.domain.port.out.UserProfileRepositoryPort;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Servicio de dominio - Lógica de negocio para perfiles de usuario
 */
public class UserProfileDomainService {
    
    private final UserProfileRepositoryPort userProfileRepository;
    private final GenderRepositoryPort genderRepository;
    
    public UserProfileDomainService(UserProfileRepositoryPort userProfileRepository,
                                    GenderRepositoryPort genderRepository) {
        this.userProfileRepository = userProfileRepository;
        this.genderRepository = genderRepository;
    }
    
    /**
     * Crea un nuevo perfil validando reglas de dominio
     */
    public UserProfile createProfile(UUID externalUserId, String firstName, String lastName,
                                     String middleName, String secondLastname, String phone,
                                     UUID genderId, String avatarUrl, LocalDate dateOfBirth) {
        
        // Validar que no existe perfil duplicado
        if (userProfileRepository.existsByExternalUserId(externalUserId)) {
            throw new UserProfileAlreadyExistsException(externalUserId);
        }
        
        // Crear el perfil
        UserProfile profile = UserProfile.create(externalUserId, firstName, lastName);
        
        // Actualizar información adicional
        profile.updatePersonalInfo(firstName, lastName, middleName, secondLastname, dateOfBirth);
        profile.updateContactInfo(phone);
        profile.updateAvatar(avatarUrl);
        
        // Asignar género si se proporciona
        if (genderId != null) {
            genderRepository.findById(genderId)
                .ifPresent(profile::assignGender);
        }
        
        return profile;
    }
    
    /**
     * Actualiza un perfil existente
     */
    public void updateProfile(UserProfile profile, String firstName, String lastName,
                              String middleName, String secondLastname, String phone,
                              UUID genderId, String avatarUrl, LocalDate dateOfBirth) {
        
        profile.updatePersonalInfo(
            firstName != null ? firstName : profile.getFirstName(),
            lastName != null ? lastName : profile.getLastName(),
            middleName,
            secondLastname,
            dateOfBirth
        );
        
        if (phone != null) {
            profile.updateContactInfo(phone);
        }
        
        if (avatarUrl != null) {
            profile.updateAvatar(avatarUrl);
        }
        
        if (genderId != null) {
            Gender gender = genderRepository.findById(genderId).orElse(null);
            profile.assignGender(gender);
        }
    }
}
