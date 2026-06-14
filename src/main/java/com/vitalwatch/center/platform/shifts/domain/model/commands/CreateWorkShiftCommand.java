package com.vitalwatch.center.platform.shifts.domain.model.commands;

import com.vitalwatch.center.platform.shifts.domain.model.enums.ShiftType;

import java.time.Instant;

/**
 * Command used to create a hospital work shift.
 */
public record CreateWorkShiftCommand(
        Long hospitalWorkspaceId,
        String label,
        String workArea,
        ShiftType shiftType,
        Instant startsAt,
        Instant endsAt
) {
}