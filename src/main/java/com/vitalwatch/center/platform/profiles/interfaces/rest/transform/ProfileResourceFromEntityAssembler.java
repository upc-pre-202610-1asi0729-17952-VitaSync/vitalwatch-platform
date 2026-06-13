package com.vitalwatch.center.platform.profiles.interfaces.rest.transform;

import com.vitalwatch.center.platform.profiles.domain.model.aggregates.Profile;
import com.vitalwatch.center.platform.profiles.interfaces.rest.resources.ProfileResource;

/**
 * Assembler to convert Profile aggregate into ProfileResource.
 */
public final class ProfileResourceFromEntityAssembler {

    private ProfileResourceFromEntityAssembler() {
    }

    public static ProfileResource toResourceFromEntity(Profile entity) {
        return new ProfileResource(
                entity.getId(),
                entity.getFullName(),
                entity.getEmailAddress(),
                entity.getStreetAddress()
        );
    }
}