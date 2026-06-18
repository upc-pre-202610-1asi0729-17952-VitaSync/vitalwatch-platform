package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible response for clinical risk assessments.
 */
@Schema(name = "FrontendRiskAssessmentResponse", description = "Risk assessment response compatible with the Angular frontend")
public record FrontendRiskAssessmentResource(
        Long id,
        Long organizationId,
        Long hospitalWorkspaceId,
        Long userAccountId,
        Long userId,
        Long vitalSignReadingId,
        Integer fatigueScore,
        Integer fatigueLevel,
        String riskLevel,
        String status,
        Integer heartRate,
        Integer hrv,
        Instant lastUpdatedAt,
        Instant assessedAt,
        Instant reviewedAt,
        Instant escalatedAt,
        Instant closedAt
) {
}