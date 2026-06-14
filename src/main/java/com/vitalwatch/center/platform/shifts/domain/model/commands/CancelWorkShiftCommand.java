package com.vitalwatch.center.platform.shifts.domain.model.commands;

/**
 * Command used to cancel a work shift.
 */
public record CancelWorkShiftCommand(
        Long workShiftId,
        Long cancelledByUserAccountId,
        String cancellationReason
) {
}