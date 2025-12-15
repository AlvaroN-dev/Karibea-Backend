package com.microservice.user.application.usecases;

import com.microservice.user.domain.events.UserProfileUpdatedEvent;
import com.microservice.user.domain.exceptions.UserProfileNotFoundException;
import com.microservice.user.domain.models.UserProfile;
import com.microservice.user.domain.port.in.UpdateUserProfileUseCase;
import com.microservice.user.domain.port.out.EventPublisherPort;
import com.microservice.user.domain.port.out.GenderRepositoryPort;
import com.microservice.user.domain.port.out.UserProfileRepositoryPort;
import com.microservice.user.domain.service.UserProfileDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementación del caso de uso para actualizar perfil de usuario
 */
@Service
@Transactional
public class UpdateUserProfileUseCaseImpl implements UpdateUserProfileUseCase {
    
    private final UserProfileDomainService domainService;
    private final UserProfileRepositoryPort userProfileRepository;
    private final EventPublisherPort eventPublisher;
    
    public UpdateUserProfileUseCaseImpl(UserProfileRepositoryPort userProfileRepository,
                                        GenderRepositoryPort genderRepository,
                                        EventPublisherPort eventPublisher) {
        this.userProfileRepository = userProfileRepository;
        this.domainService = new UserProfileDomainService(userProfileRepository, genderRepository);
        this.eventPublisher = eventPublisher;
    }
    
    @Override
    public UserProfile execute(UpdateUserProfileCommand command) {
        // Buscar perfil existente
        UserProfile profile = userProfileRepository.findById(command.profileId())
            .orElseThrow(() -> new UserProfileNotFoundException(command.profileId()));
        
        // Actualizar usando el servicio de dominio
        domainService.updateProfile(
            profile,
            command.firstName(),
            command.lastName(),
            command.middleName(),
            command.secondLastname(),
            command.phone(),
            command.genderId(),
            command.avatarUrl(),
            command.dateOfBirth()
        );
        
        // Persistir cambios
        UserProfile updatedProfile = userProfileRepository.save(profile);
        
        // Publicar evento
        Map<String, Object> changes = new HashMap<>();
        if (command.firstName() != null) changes.put("firstName", command.firstName());
        if (command.lastName() != null) changes.put("lastName", command.lastName());
        if (command.phone() != null) changes.put("phone", command.phone());
        
        eventPublisher.publish(UserProfileUpdatedEvent.create(
            updatedProfile.getId(),
            updatedProfile.getExternalUserId(),
            changes
        ));
        
        return updatedProfile;
    }
}
