package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible request for updating clinical alert status.
 */
@Schema(name = "UpdateFrontendClinicalAlertStatusRequest", description = "Clinical alert status update request compatible with Angular")
public record UpdateFrontendClinicalAlertStatusResource(
        String status,
        Instant resolvedAt,
        Long resolvedBy
) {
}