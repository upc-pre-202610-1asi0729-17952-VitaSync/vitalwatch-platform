package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform;

import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CompleteRecoveryActionCommand;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.CompleteRecoveryActionResource;

/**
 * Assembler to convert CompleteRecoveryActionResource into CompleteRecoveryActionCommand.
 */
public final class CompleteRecoveryActionCommandFromResourceAssembler {

    private CompleteRecoveryActionCommandFromResourceAssembler() {
    }

    public static CompleteRecoveryActionCommand toCommandFromResource(
            Long recoveryActionId,
            CompleteRecoveryActionResource resource
    ) {
        return new CompleteRecoveryActionCommand(
                recoveryActionId,
                resource.completedByUserAccountId()
        );
    }
}