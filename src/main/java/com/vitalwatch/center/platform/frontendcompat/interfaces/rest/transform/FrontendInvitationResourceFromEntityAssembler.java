package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendInvitationResource;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserInvitation;

/**
 * Assembler to expose UserInvitation using the contract expected by the Angular frontend.
 */
public final class FrontendInvitationResourceFromEntityAssembler {

    private FrontendInvitationResourceFromEntityAssembler() {
    }

    public static FrontendInvitationResource toResourceFromEntity(UserInvitation entity) {
        return new FrontendInvitationResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getHospitalWorkspaceId(),
                entity.getEmailAddress(),
                FrontendRoleMapper.toFrontendRole(entity.getRole()),
                entity.getStatus().name(),
                entity.getToken(),
                "/register/invitation?token=" + entity.getToken(),
                entity.getInvitedAt(),
                entity.getExpiresAt(),
                entity.getAcceptedAt()
        );
    }
}