package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible request for creating a shift record.
 */
@Schema(name = "CreateFrontendShiftRecordRequest", description = "Shift record creation request compatible with the Angular frontend")
public record CreateFrontendShiftRecordResource(
        Long organizationId,
        Long hospitalWorkspaceId,
        Long userAccountId,
        Long userId,
        Long workAreaId,
        String label,
        String name,
        String workArea,
        String shiftType,
        String type,
        String status,
        Instant scheduledStart,
        Instant scheduledEnd,
        Instant checkInAt,
        Instant checkOutAt,
        Instant startsAt,
        Instant startTime,
        Instant endsAt,
        Instant endTime
) {
}