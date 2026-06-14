package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform;

import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CancelRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.CancelRecoveryPlanResource;

/**
 * Assembler to convert CancelRecoveryPlanResource into CancelRecoveryPlanCommand.
 */
public final class CancelRecoveryPlanCommandFromResourceAssembler {

    private CancelRecoveryPlanCommandFromResourceAssembler() {
    }

    public static CancelRecoveryPlanCommand toCommandFromResource(
            Long recoveryPlanId,
            CancelRecoveryPlanResource resource
    ) {
        return new CancelRecoveryPlanCommand(
                recoveryPlanId,
                resource.cancelledByUserAccountId(),
                resource.cancellationReason()
        );
    }
}