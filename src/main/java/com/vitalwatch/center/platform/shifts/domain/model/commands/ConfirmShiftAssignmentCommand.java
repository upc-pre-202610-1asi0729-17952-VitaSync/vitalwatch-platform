package com.vitalwatch.center.platform.shifts.domain.model.commands;

/**
 * Command used to confirm a shift assignment.
 */
public record ConfirmShiftAssignmentCommand(
        Long shiftAssignmentId,
        Long confirmedByUserAccountId
) {
}