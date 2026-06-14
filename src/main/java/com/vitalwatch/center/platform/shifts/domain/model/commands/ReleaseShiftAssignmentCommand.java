package com.vitalwatch.center.platform.shifts.domain.model.commands;

/**
 * Command used to release a user from a shift assignment.
 */
public record ReleaseShiftAssignmentCommand(
        Long shiftAssignmentId,
        Long releasedByUserAccountId,
        String releaseReason
) {
}