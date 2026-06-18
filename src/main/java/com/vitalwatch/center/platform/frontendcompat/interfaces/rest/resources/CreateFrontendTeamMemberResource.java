package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for creating a team member.
 */
@Schema(name = "CreateFrontendTeamMemberRequest", description = "Team member creation request compatible with the Angular frontend")
public record CreateFrontendTeamMemberResource(
        Long teamId,
        Long careTeamId,
        Long organizationId,
        Long hospitalWorkspaceId,
        Long userAccountId,
        Long userId,
        String role
) {
}