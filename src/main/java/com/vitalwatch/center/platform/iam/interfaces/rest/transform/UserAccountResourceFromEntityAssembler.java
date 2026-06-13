package com.vitalwatch.center.platform.iam.interfaces.rest.transform;

import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserAccount;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.UserAccountResource;

/**
 * Assembler to convert UserAccount aggregate into UserAccountResource.
 */
public final class UserAccountResourceFromEntityAssembler {

    private UserAccountResourceFromEntityAssembler() {
    }

    public static UserAccountResource toResourceFromEntity(UserAccount entity) {
        return new UserAccountResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getProfileId(),
                entity.getEmailAddress(),
                entity.getRole(),
                entity.getStatus()
        );
    }
}