package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendUserResource;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserAccount;

/**
 * Assembler to expose UserAccount using the contract expected by the Angular frontend.
 */
public final class FrontendUserResourceFromEntityAssembler {

    private FrontendUserResourceFromEntityAssembler() {
    }

    public static FrontendUserResource toResourceFromEntity(UserAccount entity) {
        return new FrontendUserResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getHospitalWorkspaceId(),
                entity.getProfileId(),
                entity.getEmailAddress(),
                buildDisplayName(entity.getEmailAddress()),
                FrontendRoleMapper.toFrontendRole(entity.getRole()),
                entity.getStatus().name()
        );
    }

    private static String buildDisplayName(String email) {
        if (email == null || email.isBlank()) {
            return "User";
        }

        var atIndex = email.indexOf("@");
        return atIndex > 0 ? email.substring(0, atIndex) : email;
    }
}