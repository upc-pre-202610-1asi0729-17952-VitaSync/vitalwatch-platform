package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for creating a care team.
 */
@Schema(name = "CreateFrontendCareTeamRequest", description = "Care team creation request compatible with the Angular frontend")
public record CreateFrontendCareTeamResource(
        Long organizationId,
        Long hospitalWorkspaceId,
        String name,
        String workArea,
        Long supervisorUserId,
        Long supervisorId
) {
}