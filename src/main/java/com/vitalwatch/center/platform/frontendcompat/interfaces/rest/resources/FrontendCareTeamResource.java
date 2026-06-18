package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Frontend-compatible response for care teams.
 */
@Schema(name = "FrontendCareTeamResponse", description = "Care team response compatible with the Angular frontend")
public record FrontendCareTeamResource(
        Long id,
        Long organizationId,
        Long hospitalWorkspaceId,
        String name,
        String workArea,
        String status,
        List<Long> memberIds,
        Integer memberCount
) {
}