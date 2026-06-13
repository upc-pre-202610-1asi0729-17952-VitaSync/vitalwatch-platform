package com.vitalwatch.center.platform.iam.interfaces.rest.resources;

import com.vitalwatch.center.platform.iam.domain.model.enums.InvitationStatus;
import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource used to expose user invitation data.
 */
@Schema(name = "UserInvitationResponse", description = "Institutional user invitation response")
public record UserInvitationResource(

        @Schema(description = "Invitation id", example = "1")
        Long id,

        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @Schema(description = "Invited email", example = "doctor@clinicasangabriel.com")
        String email,

        @Schema(description = "Assigned role", example = "MEDICAL_STAFF")
        UserRole role,

        @Schema(description = "Invitation status", example = "PENDING")
        InvitationStatus status,

        @Schema(description = "Invitation token")
        String token,

        @Schema(description = "Invitation creation date")
        Instant invitedAt,

        @Schema(description = "Invitation expiration date")
        Instant expiresAt,

        @Schema(description = "Invitation acceptance date")
        Instant acceptedAt
) {
}