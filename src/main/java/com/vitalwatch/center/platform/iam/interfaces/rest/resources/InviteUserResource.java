package com.vitalwatch.center.platform.iam.interfaces.rest.resources;

import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to invite a hospital user.
 */
@Schema(name = "InviteUserRequest", description = "Request payload for inviting a hospital user")
public record InviteUserResource(

        @NotNull
        @Positive
        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @NotBlank(message = "{validation.not-blank}")
        @Email(message = "{validation.email}")
        @Schema(description = "Invited user email", example = "doctor@clinicasangabriel.com")
        String email,

        @NotNull
        @Schema(description = "Institutional role", example = "MEDICAL_STAFF")
        UserRole role
) {
}