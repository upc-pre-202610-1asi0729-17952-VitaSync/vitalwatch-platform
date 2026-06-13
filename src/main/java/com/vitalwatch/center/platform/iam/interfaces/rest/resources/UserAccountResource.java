package com.vitalwatch.center.platform.iam.interfaces.rest.resources;

import com.vitalwatch.center.platform.iam.domain.model.enums.UserAccountStatus;
import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource used to expose user account data.
 */
@Schema(name = "UserAccountResponse", description = "Institutional user account response")
public record UserAccountResource(

        @Schema(description = "User account id", example = "1")
        Long id,

        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @Schema(description = "Profile id", example = "1")
        Long profileId,

        @Schema(description = "User email", example = "doctor@clinicasangabriel.com")
        String email,

        @Schema(description = "Institutional role", example = "MEDICAL_STAFF")
        UserRole role,

        @Schema(description = "Account status", example = "ACTIVE")
        UserAccountStatus status
) {
}