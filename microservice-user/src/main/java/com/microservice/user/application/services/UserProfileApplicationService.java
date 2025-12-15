package com.microservice.user.application.services;

import com.microservice.user.application.dto.request.CreateUserProfileRequest;
import com.microservice.user.application.dto.request.UpdateUserProfileRequest;
import com.microservice.user.application.dto.response.PagedResponse;
import com.microservice.user.application.dto.response.UserProfileResponse;
import com.microservice.user.application.mapper.UserProfileDtoMapper;
import com.microservice.user.domain.exceptions.UserProfileNotFoundException;
import com.microservice.user.domain.models.UserProfile;
import com.microservice.user.domain.port.in.CreateUserProfileUseCase;
import com.microservice.user.domain.port.in.CreateUserProfileUseCase.CreateUserProfileCommand;
import com.microservice.user.domain.port.in.DeleteUserProfileUseCase;
import com.microservice.user.domain.port.in.GetUserProfileUseCase;
import com.microservice.user.domain.port.in.UpdateUserProfileUseCase;
import com.microservice.user.domain.port.in.UpdateUserProfileUseCase.UpdateUserProfileCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Servicio de aplicación para perfiles de usuario
 * Orquesta los casos de uso y realiza el mapeo de DTOs
 */
@Service
public class UserProfileApplicationService {
    
    private final CreateUserProfileUseCase createUserProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final GetUserProfileUseCase getUserProfileUseCase;
    private final DeleteUserProfileUseCase deleteUserProfileUseCase;
    private final UserProfileDtoMapper mapper;
    
    public UserProfileApplicationService(CreateUserProfileUseCase createUserProfileUseCase,
                                         UpdateUserProfileUseCase updateUserProfileUseCase,
                                         GetUserProfileUseCase getUserProfileUseCase,
                                         DeleteUserProfileUseCase deleteUserProfileUseCase,
                                         UserProfileDtoMapper mapper) {
        this.createUserProfileUseCase = createUserProfileUseCase;
        this.updateUserProfileUseCase = updateUserProfileUseCase;
        this.getUserProfileUseCase = getUserProfileUseCase;
        this.deleteUserProfileUseCase = deleteUserProfileUseCase;
        this.mapper = mapper;
    }
    
    public UserProfileResponse createProfile(CreateUserProfileRequest request) {
        CreateUserProfileCommand command = new CreateUserProfileCommand(
            request.getExternalUserId(),
            request.getFirstName(),
            request.getLastName(),
            request.getMiddleName(),
            request.getSecondLastname(),
            request.getPhone(),
            request.getGenderId(),
            request.getAvatarUrl(),
            request.getDateOfBirth()
        );
        
        UserProfile profile = createUserProfileUseCase.execute(command);
        return mapper.toResponse(profile);
    }
    
    public UserProfileResponse updateProfile(UUID profileId, UpdateUserProfileRequest request) {
        UpdateUserProfileCommand command = new UpdateUserProfileCommand(
            profileId,
            request.getFirstName(),
            request.getLastName(),
            request.getMiddleName(),
            request.getSecondLastname(),
            request.getPhone(),
            request.getGenderId(),
            request.getAvatarUrl(),
            request.getDateOfBirth()
        );
        
        UserProfile profile = updateUserProfileUseCase.execute(command);
        return mapper.toResponse(profile);
    }
    
    public UserProfileResponse getProfileById(UUID profileId) {
        UserProfile profile = getUserProfileUseCase.findById(profileId)
            .orElseThrow(() -> new UserProfileNotFoundException(profileId));
        return mapper.toResponse(profile);
    }
    
    public UserProfileResponse getProfileByExternalUserId(UUID externalUserId) {
        UserProfile profile = getUserProfileUseCase.findByExternalUserId(externalUserId)
            .orElseThrow(() -> UserProfileNotFoundException.byExternalUserId(externalUserId));
        return mapper.toResponse(profile);
    }
    
    public PagedResponse<UserProfileResponse> getAllProfiles(Pageable pageable) {
        Page<UserProfile> page = getUserProfileUseCase.findAll(pageable);
        
        return PagedResponse.of(
            page.getContent().stream().map(mapper::toResponse).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
    
    public void deleteProfile(UUID profileId) {
        deleteUserProfileUseCase.execute(profileId);
    }
}
