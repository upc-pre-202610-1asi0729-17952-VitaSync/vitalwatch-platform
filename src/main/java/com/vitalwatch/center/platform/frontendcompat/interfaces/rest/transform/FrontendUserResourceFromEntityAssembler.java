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
        var firstName = buildFirstName(entity.getEmailAddress());
        var lastName = "User";
        var fullName = firstName + " " + lastName;

        return new FrontendUserResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getHospitalWorkspaceId(),
                entity.getProfileId(),
                firstName,
                lastName,
                fullName,
                fullName,
                entity.getEmailAddress(),
                "password",
                "999999999",
                1L,
                1L,
                FrontendRoleMapper.toFrontendRole(entity.getRole()),
                toFrontendStatus(entity.getStatus().name())
        );
    }

    private static String buildFirstName(String email) {
        if (email == null || email.isBlank()) {
            return "VitalWatch";
        }

        var atIndex = email.indexOf("@");
        var rawName = atIndex > 0 ? email.substring(0, atIndex) : email;

        if (rawName.isBlank()) {
            return "VitalWatch";
        }

        return rawName.substring(0, 1).toUpperCase() + rawName.substring(1);
    }

    private static String toFrontendStatus(String status) {
        return switch (status) {
            case "ACTIVE" -> "ACTIVE";
            case "PENDING" -> "PENDING";
            default -> "INACTIVE";
        };
    }
}