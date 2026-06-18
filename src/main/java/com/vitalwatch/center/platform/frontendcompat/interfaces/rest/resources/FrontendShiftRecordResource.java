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
        String label,
        String name,
        String workArea,
        String shiftType,
        String type,
        String status,
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