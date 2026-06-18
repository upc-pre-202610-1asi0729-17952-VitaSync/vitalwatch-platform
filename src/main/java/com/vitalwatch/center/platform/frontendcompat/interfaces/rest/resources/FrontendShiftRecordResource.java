package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible response for shift records.
 */
@Schema(name = "FrontendShiftRecordResponse", description = "Shift record response compatible with the Angular frontend")
public record FrontendShiftRecordResource(
        Long id,
        Long organizationId,
        Long hospitalWorkspaceId,
        Long userAccountId,
        Long userId,
        Long workAreaId,
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
        Instant endTime,
        Long completedByUserAccountId,
        Long cancelledByUserAccountId,
        String cancellationReason,
        Instant completedAt,
        Instant cancelledAt
) {
}