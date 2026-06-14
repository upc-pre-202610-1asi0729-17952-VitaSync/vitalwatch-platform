package com.vitalwatch.center.platform.shifts.domain.model.commands;

/**
 * Command used to complete a work shift.
 */
public record CompleteWorkShiftCommand(
        Long workShiftId,
        Long completedByUserAccountId
) {
}