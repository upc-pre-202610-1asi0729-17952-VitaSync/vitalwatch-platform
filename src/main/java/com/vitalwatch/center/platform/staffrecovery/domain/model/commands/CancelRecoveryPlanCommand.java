package com.vitalwatch.center.platform.staffrecovery.domain.model.commands;

/**
 * Command used to cancel a staff recovery plan.
 */
public record CancelRecoveryPlanCommand(
        Long recoveryPlanId,
        Long cancelledByUserAccountId,
        String cancellationReason
) {
}