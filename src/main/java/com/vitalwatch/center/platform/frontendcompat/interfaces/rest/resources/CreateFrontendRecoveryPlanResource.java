package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for creating a recovery plan.
 */
@Schema(name = "CreateFrontendRecoveryPlanRequest", description = "Recovery plan creation request compatible with the Angular frontend")
public record CreateFrontendRecoveryPlanResource(
        Long organizationId,
        Long hospitalWorkspaceId,
        Long userAccountId,
        Long userId,
        Long clinicalRiskAssessmentId,
        Long incidentId,
        String reason,
        String priority,
        Double recommendedRestHours,
        String notes
) {
}