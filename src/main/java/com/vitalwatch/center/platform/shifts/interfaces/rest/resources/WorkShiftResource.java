package com.vitalwatch.center.platform.shifts.interfaces.rest.resources;

import com.vitalwatch.center.platform.shifts.domain.model.enums.ShiftStatus;
import com.vitalwatch.center.platform.shifts.domain.model.enums.ShiftType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource used to expose work shift data.
 */
@Schema(name = "WorkShiftResponse", description = "Work shift information response")
public record WorkShiftResource(

        @Schema(description = "Work shift id", example = "1")
        Long id,

        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @Schema(description = "Shift label", example = "Emergency Night Shift")
        String label,

        @Schema(description = "Hospital work area", example = "Emergency")
        String workArea,

        @Schema(description = "Shift type", example = "NIGHT")
        ShiftType shiftType,

        @Schema(description = "Shift status", example = "PLANNED")
        ShiftStatus status,

        @Schema(description = "Shift start date")
        Instant startsAt,

        @Schema(description = "Shift end date")
        Instant endsAt,

        @Schema(description = "User account id that completed the shift")
        Long completedByUserAccountId,

        @Schema(description = "User account id that cancelled the shift")
        Long cancelledByUserAccountId,

        @Schema(description = "Cancellation reason")
        String cancellationReason,

        @Schema(description = "Completion date")
        Instant completedAt,

        @Schema(description = "Cancellation date")
        Instant cancelledAt
) {
}