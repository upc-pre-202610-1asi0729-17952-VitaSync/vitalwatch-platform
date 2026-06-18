package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible response for team members.
 */
@Schema(name = "FrontendTeamMemberResponse", description = "Team member response compatible with the Angular frontend")
public record FrontendTeamMemberResource(
        Long id,
        Long teamId,
        Long careTeamId,
        Long organizationId,
        Long hospitalWorkspaceId,
        Long userAccountId,
        Long userId,
        Long profileId,
        String email,
        String name,
        String role,
        String status,
        Instant assignedAt
) {
}