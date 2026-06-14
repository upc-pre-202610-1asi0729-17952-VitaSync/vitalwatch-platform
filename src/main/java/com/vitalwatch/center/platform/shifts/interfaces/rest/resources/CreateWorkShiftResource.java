package com.vitalwatch.center.platform.shifts.interfaces.rest.resources;

import com.vitalwatch.center.platform.shifts.domain.model.enums.ShiftType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;

/**
 * Resource used to create a hospital work shift.
 */
@Schema(name = "CreateWorkShiftRequest", description = "Request payload for creating a work shift")
public record CreateWorkShiftResource(

        @NotNull
        @Positive
        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Shift label", example = "Emergency Night Shift")
        String label,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Hospital work area", example = "Emergency")
        String workArea,

        @NotNull
        @Schema(description = "Shift type", example = "NIGHT")
        ShiftType shiftType,

        @NotNull
        @Schema(description = "Shift start date", example = "2026-06-14T00:00:00Z")
        Instant startsAt,

        @NotNull
        @Schema(description = "Shift end date", example = "2026-06-14T08:00:00Z")
        Instant endsAt
) {
}