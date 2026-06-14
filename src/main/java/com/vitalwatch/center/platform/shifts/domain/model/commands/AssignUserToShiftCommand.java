package com.vitalwatch.center.platform.shifts.domain.model.commands;

/**
 * Command used to assign a user account to a work shift.
 */
public record AssignUserToShiftCommand(
        Long workShiftId,
        Long userAccountId,
        Long assignedByUserAccountId
) {
}