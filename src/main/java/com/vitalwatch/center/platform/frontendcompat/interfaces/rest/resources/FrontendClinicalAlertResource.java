package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible response for clinical alerts.
 */
@Schema(name = "FrontendClinicalAlertResponse", description = "Clinical alert response compatible with the Angular frontend")
public record FrontendClinicalAlertResource(
        Long id,
        Long organizationId,
        Long hospitalWorkspaceId,
        Long userAccountId,
        Long userId,
        Long riskAssessmentId,
        String type,
        String severity,
        String message,
        String status,
        Instant createdAt
) {
}