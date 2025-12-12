package com.microservice.user.application.usecases;

import com.microservice.user.domain.events.UserProfileCreatedEvent;
import com.microservice.user.domain.models.UserProfile;
import com.microservice.user.domain.port.in.CreateUserProfileUseCase;
import com.microservice.user.domain.port.out.EventPublisherPort;
import com.microservice.user.domain.port.out.GenderRepositoryPort;
import com.microservice.user.domain.port.out.UserProfileRepositoryPort;
import com.microservice.user.domain.service.UserProfileDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso para crear perfil de usuario
 */
@Service
@Transactional
public class CreateUserProfileUseCaseImpl implements CreateUserProfileUseCase {
    
    private final UserProfileDomainService domainService;
    private final UserProfileRepositoryPort userProfileRepository;
    private final EventPublisherPort eventPublisher;
    
    public CreateUserProfileUseCaseImpl(UserProfileRepositoryPort userProfileRepository,
                                        GenderRepositoryPort genderRepository,
                                        EventPublisherPort eventPublisher) {
        this.userProfileRepository = userProfileRepository;
        this.domainService = new UserProfileDomainService(userProfileRepository, genderRepository);
        this.eventPublisher = eventPublisher;
    }
    
    @Override
    public UserProfile execute(CreateUserProfileCommand command) {
        // Crear perfil usando el servicio de dominio
        UserProfile profile = domainService.createProfile(
            command.externalUserId(),
            command.firstName(),
            command.lastName(),
            command.middleName(),
            command.secondLastname(),
            command.phone(),
            command.genderId(),
            command.avatarUrl(),
            command.dateOfBirth()
        );
        
        // Persistir
        UserProfile savedProfile = userProfileRepository.save(profile);
        
        // Publicar evento
        eventPublisher.publish(UserProfileCreatedEvent.create(
            savedProfile.getId(),
            savedProfile.getExternalUserId(),
            savedProfile.getFirstName(),
            savedProfile.getLastName()
        ));
        
        return savedProfile;
    }
}
