package com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.resources;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.enums.VitalSignSource;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource used to expose vital sign reading data.
 */
@Schema(name = "VitalSignReadingResponse", description = "Vital sign and workload data response")
public record VitalSignReadingResource(

        @Schema(description = "Vital sign reading id", example = "1")
        Long id,

        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @Schema(description = "User account id", example = "1")
        Long userAccountId,

        @Schema(description = "Heart rate in bpm", example = "110")
        Integer heartRateBpm,

        @Schema(description = "Sleep hours during the last 24 hours", example = "4.5")
        Double sleepHoursLast24h,

        @Schema(description = "Shift hours during the last 24 hours", example = "12.0")
        Double shiftHoursLast24h,

        @Schema(description = "Self-reported fatigue level", example = "4")
        Integer selfReportedFatigueLevel,

        @Schema(description = "Data source", example = "MANUAL_ENTRY")
        VitalSignSource source,

        @Schema(description = "Reading record date")
        Instant recordedAt
) {
}