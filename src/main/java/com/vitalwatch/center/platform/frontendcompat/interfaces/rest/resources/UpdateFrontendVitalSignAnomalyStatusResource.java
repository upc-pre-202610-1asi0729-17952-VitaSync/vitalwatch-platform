package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible request for updating vital sign anomaly status.
 */
@Schema(name = "UpdateFrontendVitalSignAnomalyStatusRequest", description = "Vital sign anomaly status update request compatible with Angular")
public record UpdateFrontendVitalSignAnomalyStatusResource(
        String status,
        Instant reviewedAt,
        Long reviewedBy
) {
}