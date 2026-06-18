package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible response for preventive actions.
 */
@Schema(name = "FrontendPreventiveActionResponse", description = "Preventive action response compatible with the Angular frontend")
public record FrontendPreventiveActionResource(
        Long id,
        Long recoveryPlanId,
        Long organizationId,
        Long hospitalWorkspaceId,
        Long userAccountId,
        Long userId,
        Long supervisorId,
        Long supervisorUserId,
        String type,
        String actionType,
        String notes,
        Double recommendedRestHours,
        Boolean completed,
        String status,
        Long completedByUserAccountId,
        Instant createdAt,
        Instant completedAt
) {
}