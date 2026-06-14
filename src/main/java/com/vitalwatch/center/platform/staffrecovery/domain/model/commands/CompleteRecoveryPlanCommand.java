package com.vitalwatch.center.platform.staffrecovery.domain.model.commands;

/**
 * Command used to complete a staff recovery plan.
 */
public record CompleteRecoveryPlanCommand(
        Long recoveryPlanId,
        Long completedByUserAccountId,
        String completionNotes
) {
}