package com.vitalwatch.center.platform.staffrecovery.domain.model.commands;

/**
 * Command used to start a staff recovery plan.
 */
public record StartRecoveryPlanCommand(
        Long recoveryPlanId,
        Long startedByUserAccountId
) {
}