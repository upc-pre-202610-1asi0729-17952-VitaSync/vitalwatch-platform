package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for patching care teams.
 */
@Schema(name = "PatchFrontendCareTeamRequest", description = "Care team partial update request compatible with Angular")
public record PatchFrontendCareTeamResource(
        String name,
        Long workAreaId,
        String workArea,
        Long supervisorId,
        Long supervisorUserId,
        String status
) {
}