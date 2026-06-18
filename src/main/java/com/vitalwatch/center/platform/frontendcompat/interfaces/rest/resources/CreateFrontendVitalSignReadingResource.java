package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for registering a vital sign reading.
 */
@Schema(name = "CreateFrontendVitalSignReadingRequest", description = "Vital sign reading creation request compatible with the Angular frontend")
public record CreateFrontendVitalSignReadingResource(
        Long organizationId,
        Long hospitalWorkspaceId,
        Long userAccountId,
        Long userId,
        Integer heartRateBpm,
        Integer bpm,
        Integer heartRate,
        Double sleepHoursLast24h,
        Double shiftHoursLast24h,
        Integer selfReportedFatigueLevel,
        String source
) {
}