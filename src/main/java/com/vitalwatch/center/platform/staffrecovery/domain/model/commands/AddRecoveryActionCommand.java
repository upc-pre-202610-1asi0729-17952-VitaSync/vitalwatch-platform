package com.vitalwatch.center.platform.staffrecovery.domain.model.commands;

import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryActionType;

/**
 * Command used to add a recovery action to a plan.
 */
public record AddRecoveryActionCommand(
        Long recoveryPlanId,
        RecoveryActionType actionType,
        String notes,
        Double recommendedRestHours
) {
}