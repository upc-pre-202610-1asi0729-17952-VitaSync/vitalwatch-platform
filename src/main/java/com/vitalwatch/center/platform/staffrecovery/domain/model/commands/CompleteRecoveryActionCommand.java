package com.vitalwatch.center.platform.staffrecovery.domain.model.commands;

/**
 * Command used to complete a recovery action.
 */
public record CompleteRecoveryActionCommand(
        Long recoveryActionId,
        Long completedByUserAccountId
) {
}