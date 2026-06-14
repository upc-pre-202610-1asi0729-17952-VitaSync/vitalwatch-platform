package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform;

import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.StartRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.StartRecoveryPlanResource;

/**
 * Assembler to convert StartRecoveryPlanResource into StartRecoveryPlanCommand.
 */
public final class StartRecoveryPlanCommandFromResourceAssembler {

    private StartRecoveryPlanCommandFromResourceAssembler() {
    }

    public static StartRecoveryPlanCommand toCommandFromResource(
            Long recoveryPlanId,
            StartRecoveryPlanResource resource
    ) {
        return new StartRecoveryPlanCommand(
                recoveryPlanId,
                resource.startedByUserAccountId()
        );
    }
}