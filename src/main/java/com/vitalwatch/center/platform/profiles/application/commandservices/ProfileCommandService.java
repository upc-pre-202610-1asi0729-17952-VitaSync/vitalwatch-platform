package com.vitalwatch.center.platform.profiles.application.commandservices;

import com.vitalwatch.center.platform.profiles.domain.model.aggregates.Profile;
import com.vitalwatch.center.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;

/**
 * Application service contract for profile commands.
 */
public interface ProfileCommandService {

    Result<Profile, ApplicationError> handle(CreateProfileCommand command);
}