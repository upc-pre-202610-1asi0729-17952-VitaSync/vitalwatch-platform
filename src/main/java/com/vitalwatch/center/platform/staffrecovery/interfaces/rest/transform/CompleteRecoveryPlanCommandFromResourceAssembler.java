package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform;

import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CompleteRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.CompleteRecoveryPlanResource;

/**
 * Assembler to convert CompleteRecoveryPlanResource into CompleteRecoveryPlanCommand.
 */
public final class CompleteRecoveryPlanCommandFromResourceAssembler {

    private CompleteRecoveryPlanCommandFromResourceAssembler() {
    }

    public static CompleteRecoveryPlanCommand toCommandFromResource(
            Long recoveryPlanId,
            CompleteRecoveryPlanResource resource
    ) {
        return new CompleteRecoveryPlanCommand(
                recoveryPlanId,
                resource.completedByUserAccountId(),
                resource.completionNotes()
        );
    }
}