package com.vitalwatch.center.platform.profiles.application.acl;

import com.vitalwatch.center.platform.profiles.application.commandservices.ProfileCommandService;
import com.vitalwatch.center.platform.profiles.application.queryservices.ProfileQueryService;
import com.vitalwatch.center.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.vitalwatch.center.platform.profiles.domain.model.queries.GetProfileByEmailQuery;
import com.vitalwatch.center.platform.profiles.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

/**
 * Application-layer implementation of Profiles ACL facade.
 */
@Service
public class ProfilesContextFacadeImpl implements ProfilesContextFacade {

    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    public ProfilesContextFacadeImpl(
            ProfileCommandService profileCommandService,
            ProfileQueryService profileQueryService
    ) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
    }

    @Override
    public Long createProfile(
            String firstName,
            String lastName,
            String email,
            String street,
            String number,
            String city,
            String postalCode,
            String country
    ) {
        var command = new CreateProfileCommand(
                firstName,
                lastName,
                email,
                street,
                number,
                city,
                postalCode,
                country
        );

        var result = profileCommandService.handle(command);

        return result.toOptional()
                .map(profile -> profile.getId())
                .orElse(0L);
    }

    @Override
    public Long fetchProfileIdByEmail(String email) {
        var query = new GetProfileByEmailQuery(new EmailAddress(email));
        var profile = profileQueryService.handle(query);

        return profile.map(value -> value.getId()).orElse(0L);
    }
}