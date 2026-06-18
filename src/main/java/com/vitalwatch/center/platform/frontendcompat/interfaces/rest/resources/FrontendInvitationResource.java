package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible response for invitations.
 */
@Schema(name = "FrontendInvitationResponse", description = "Invitation response compatible with the Angular frontend")
public record FrontendInvitationResource(
        Long id,
        Long organizationId,
        Long hospitalWorkspaceId,
        String email,
        String role,
        String status,
        String token,
        String link,
        Instant invitedAt,
        Instant expiresAt,
        Instant acceptedAt
) {
}