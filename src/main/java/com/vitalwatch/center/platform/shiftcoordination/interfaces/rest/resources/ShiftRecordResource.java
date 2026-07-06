package com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources;

import java.time.OffsetDateTime;

/**
 * Resource used to expose shift record data.
 */
public record ShiftRecordResource(
        Long id,
        Long organizationId,
        Long userId,
        Long workAreaId,
        String type,
        String status,
        OffsetDateTime scheduledStart,
        OffsetDateTime scheduledEnd,
        OffsetDateTime checkInAt,
        OffsetDateTime checkOutAt
) {
}