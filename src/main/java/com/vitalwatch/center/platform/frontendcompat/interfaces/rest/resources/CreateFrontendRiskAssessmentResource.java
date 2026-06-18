package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for creating a risk assessment.
 */
@Schema(name = "CreateFrontendRiskAssessmentRequest", description = "Risk assessment creation request compatible with the Angular frontend")
public record CreateFrontendRiskAssessmentResource(
        Long organizationId,
        Long hospitalWorkspaceId,
        Long userAccountId,
        Long userId,
        Long vitalSignReadingId
) {
}