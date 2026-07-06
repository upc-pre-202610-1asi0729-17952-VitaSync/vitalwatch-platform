package com.vitalwatch.center.platform.iam.invitations.interfaces.rest.resources;

import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Resource used to send an invitation.
 */
public record SendInvitationResource(
        @NotNull
        Long organizationId,

        @NotBlank
        @Email
        @Size(max = 120)
        String email,

        @NotNull
        UserRole role,

        Long specialtyId,

        Long workAreaId
) {
}