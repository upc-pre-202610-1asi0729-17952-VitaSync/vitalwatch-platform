package com.vitalwatch.center.platform.profiles.application.internal.commandservices;

import com.vitalwatch.center.platform.profiles.application.commandservices.ProfileCommandService;
import com.vitalwatch.center.platform.profiles.domain.model.aggregates.Profile;
import com.vitalwatch.center.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.vitalwatch.center.platform.profiles.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.profiles.domain.repositories.ProfileRepository;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Profile command service implementation.
 */
@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {

    private final ProfileRepository profileRepository;

    public ProfileCommandServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Result<Profile, ApplicationError> handle(CreateProfileCommand command) {
        try {
            var emailAddress = new EmailAddress(command.email());

            if (profileRepository.existsByEmailAddress(emailAddress)) {
                return Result.failure(ApplicationError.conflict(
                        "Profile",
                        "A profile with email address '%s' already exists".formatted(command.email())
                ));
            }

            var profile = new Profile(command);
            var savedProfile = profileRepository.save(profile);

            return Result.success(savedProfile);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("Profile", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Profile creation", exception.getMessage()));
        }
    }
}