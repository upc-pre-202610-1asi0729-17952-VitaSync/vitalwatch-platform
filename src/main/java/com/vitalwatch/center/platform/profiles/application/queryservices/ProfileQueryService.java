package com.vitalwatch.center.platform.profiles.application.queryservices;

import com.vitalwatch.center.platform.profiles.domain.model.aggregates.Profile;
import com.vitalwatch.center.platform.profiles.domain.model.queries.GetAllProfilesQuery;
import com.vitalwatch.center.platform.profiles.domain.model.queries.GetProfileByEmailQuery;
import com.vitalwatch.center.platform.profiles.domain.model.queries.GetProfileByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for profile queries.
 */
public interface ProfileQueryService {

    Optional<Profile> handle(GetProfileByIdQuery query);

    Optional<Profile> handle(GetProfileByEmailQuery query);

    List<Profile> handle(GetAllProfilesQuery query);
}