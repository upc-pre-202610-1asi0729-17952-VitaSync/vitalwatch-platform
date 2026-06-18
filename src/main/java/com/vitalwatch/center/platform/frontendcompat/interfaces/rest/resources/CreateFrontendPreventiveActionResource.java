package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for creating a preventive action.
 */
@Schema(name = "CreateFrontendPreventiveActionRequest", description = "Preventive action creation request compatible with the Angular frontend")
public record CreateFrontendPreventiveActionResource(
        Long recoveryPlanId,
        Long organizationId,
        Long hospitalWorkspaceId,
        Long supervisorId,
        Long supervisorUserId,
        Long userAccountId,
        Long userId,
        String type,
        String actionType,
        String notes,
        Double recommendedRestHours
) {
}