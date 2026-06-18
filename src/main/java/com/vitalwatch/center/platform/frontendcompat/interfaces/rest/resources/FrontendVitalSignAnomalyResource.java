package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible response for vital sign anomalies.
 */
@Schema(name = "FrontendVitalSignAnomalyResponse", description = "Vital sign anomaly response compatible with the Angular frontend")
public record FrontendVitalSignAnomalyResource(
        Long id,
        Long organizationId,
        Long hospitalWorkspaceId,
        Long userAccountId,
        Long userId,
        Long vitalSignReadingId,
        String type,
        String severity,
        String status,
        String metric,
        Double value,
        Double threshold,
        String message,
        Instant detectedAt,
        Instant reviewedAt,
        Long reviewedBy
) {
}