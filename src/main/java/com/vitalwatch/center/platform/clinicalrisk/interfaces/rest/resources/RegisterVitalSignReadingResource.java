package com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.resources;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.enums.VitalSignSource;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource used to register staff physiological and workload data.
 */
@Schema(name = "RegisterVitalSignReadingRequest", description = "Request payload for registering vital sign and workload data")
public record RegisterVitalSignReadingResource(

        @NotNull
        @Positive
        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @NotNull
        @Positive
        @Schema(description = "User account id", example = "1")
        Long userAccountId,

        @NotNull
        @Min(30)
        @Max(240)
        @Schema(description = "Heart rate in beats per minute", example = "110")
        Integer heartRateBpm,

        @NotNull
        @Min(0)
        @Max(24)
        @Schema(description = "Sleep hours during the last 24 hours", example = "4.5")
        Double sleepHoursLast24h,

        @NotNull
        @Min(0)
        @Max(24)
        @Schema(description = "Shift hours during the last 24 hours", example = "12.0")
        Double shiftHoursLast24h,

        @NotNull
        @Min(1)
        @Max(5)
        @Schema(description = "Self-reported fatigue level from 1 to 5", example = "4")
        Integer selfReportedFatigueLevel,

        @NotNull
        @Schema(description = "Data source", example = "MANUAL_ENTRY")
        VitalSignSource source
) {
}