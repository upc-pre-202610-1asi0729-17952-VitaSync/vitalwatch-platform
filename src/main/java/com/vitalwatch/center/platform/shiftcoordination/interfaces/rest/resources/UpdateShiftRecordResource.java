package com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources;

import com.vitalwatch.center.platform.shiftcoordination.domain.model.enums.ShiftStatus;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

/**
 * Resource used to update shift status, check-in and check-out data.
 */
public record UpdateShiftRecordResource(
        @NotNull
        ShiftStatus status,

        OffsetDateTime checkInAt,

        OffsetDateTime checkOutAt
) {
}