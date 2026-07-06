package com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources;

import com.vitalwatch.center.platform.shiftcoordination.domain.model.enums.ShiftStatus;
import com.vitalwatch.center.platform.shiftcoordination.domain.model.enums.ShiftType;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

/**
 * Resource used to create a shift record.
 */
public record CreateShiftRecordResource(
        @NotNull
        Long organizationId,

        @NotNull
        Long userId,

        @NotNull
        Long workAreaId,

        @NotNull
        ShiftType type,

        ShiftStatus status,

        @NotNull
        OffsetDateTime scheduledStart,

        @NotNull
        OffsetDateTime scheduledEnd,

        OffsetDateTime checkInAt,

        OffsetDateTime checkOutAt
) {
}