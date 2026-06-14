package com.vitalwatch.center.platform.staffrecovery.domain.model.commands;

import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryPlanReason;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryPriority;

/**
 * Command used to create a staff recovery plan.
 */
public record CreateRecoveryPlanCommand(
        Long hospitalWorkspaceId,
        Long userAccountId,
        Long clinicalRiskAssessmentId,
        Long incidentId,
        RecoveryPlanReason reason,
        RecoveryPriority priority,
        Double recommendedRestHours,
        String notes
) {
}