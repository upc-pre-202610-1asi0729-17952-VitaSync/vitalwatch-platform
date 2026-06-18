package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Frontend-compatible request for creating an invitation.
 */
@Schema(name = "CreateFrontendInvitationRequest", description = "Invitation creation request compatible with the Angular frontend")
public record CreateFrontendInvitationResource(
        Long organizationId,
        Long hospitalWorkspaceId,

        @NotBlank(message = "{validation.not-blank}")
        @Email(message = "{validation.email}")
        String email,

        @NotBlank(message = "{validation.not-blank}")
        String role
) {
}