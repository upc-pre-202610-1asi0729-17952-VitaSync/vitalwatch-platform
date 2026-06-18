package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible request for updating shift record status.
 */
@Schema(name = "UpdateFrontendShiftRecordStatusRequest", description = "Shift record status update request compatible with Angular")
public record UpdateFrontendShiftRecordStatusResource(
        String status,
        Instant checkInAt,
        Instant checkOutAt,
        Long userAccountId,
        Long userId
) {
}