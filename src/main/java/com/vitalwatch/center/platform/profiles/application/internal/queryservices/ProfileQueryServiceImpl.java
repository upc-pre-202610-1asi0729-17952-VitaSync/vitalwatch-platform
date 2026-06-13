package com.vitalwatch.center.platform.profiles.application.internal.queryservices;

import com.vitalwatch.center.platform.profiles.application.queryservices.ProfileQueryService;
import com.vitalwatch.center.platform.profiles.domain.model.aggregates.Profile;
import com.vitalwatch.center.platform.profiles.domain.model.queries.GetAllProfilesQuery;
import com.vitalwatch.center.platform.profiles.domain.model.queries.GetProfileByEmailQuery;
import com.vitalwatch.center.platform.profiles.domain.model.queries.GetProfileByIdQuery;
import com.vitalwatch.center.platform.profiles.domain.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Profile query service implementation.
 */
@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final ProfileRepository profileRepository;

    public ProfileQueryServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<Profile> handle(GetProfileByIdQuery query) {
        return profileRepository.findById(query.profileId());
    }

    @Override
    public Optional<Profile> handle(GetProfileByEmailQuery query) {
        return profileRepository.findByEmailAddress(query.emailAddress());
    }

    @Override
    public List<Profile> handle(GetAllProfilesQuery query) {
        return profileRepository.findAll();
    }
}