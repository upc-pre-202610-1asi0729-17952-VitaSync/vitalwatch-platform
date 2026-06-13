package com.vitalwatch.center.platform.iam.interfaces.rest.transform;

import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserInvitation;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.UserInvitationResource;

/**
 * Assembler to convert UserInvitation aggregate into UserInvitationResource.
 */
public final class UserInvitationResourceFromEntityAssembler {

    private UserInvitationResourceFromEntityAssembler() {
    }

    public static UserInvitationResource toResourceFromEntity(UserInvitation entity) {
        return new UserInvitationResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getEmailAddress(),
                entity.getRole(),
                entity.getStatus(),
                entity.getToken(),
                entity.getInvitedAt(),
                entity.getExpiresAt(),
                entity.getAcceptedAt()
        );
    }
}