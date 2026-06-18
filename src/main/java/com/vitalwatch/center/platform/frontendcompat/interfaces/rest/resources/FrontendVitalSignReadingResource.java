package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible response for vital sign readings.
 */
@Schema(name = "FrontendVitalSignReadingResponse", description = "Vital sign reading response compatible with the Angular frontend")
public record FrontendVitalSignReadingResource(
        Long id,
        Long organizationId,
        Long hospitalWorkspaceId,
        Long userAccountId,
        Long userId,
        Integer heartRate,
        Integer heartRateBpm,
        Integer bpm,
        Integer hrv,
        Integer fatigueLevel,
        Double cortisolLevel,
        String sensorStatus,
        Double sleepHoursLast24h,
        Double shiftHoursLast24h,
        Integer selfReportedFatigueLevel,
        String source,
        Instant recordedAt
) {
}