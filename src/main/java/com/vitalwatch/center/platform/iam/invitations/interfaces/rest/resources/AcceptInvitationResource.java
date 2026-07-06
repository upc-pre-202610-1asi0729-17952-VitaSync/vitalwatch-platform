package com.vitalwatch.center.platform.iam.invitations.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Resource used to accept an invitation and create a user account.
 */
public record AcceptInvitationResource(
        @NotBlank
        String token,

        @NotBlank
        @Size(max = 80)
        String firstName,

        @NotBlank
        @Size(max = 80)
        String lastName,

        @NotBlank
        @Size(max = 80)
        String username,

        @NotBlank
        @Size(min = 6, max = 80)
        String password,

        Long specialtyId,

        Long workAreaId
) {
}