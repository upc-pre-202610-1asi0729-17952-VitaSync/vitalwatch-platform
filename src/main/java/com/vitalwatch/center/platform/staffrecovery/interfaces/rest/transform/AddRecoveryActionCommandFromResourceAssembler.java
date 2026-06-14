package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform;

import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.AddRecoveryActionCommand;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.AddRecoveryActionResource;

/**
 * Assembler to convert AddRecoveryActionResource into AddRecoveryActionCommand.
 */
public final class AddRecoveryActionCommandFromResourceAssembler {

    private AddRecoveryActionCommandFromResourceAssembler() {
    }

    public static AddRecoveryActionCommand toCommandFromResource(AddRecoveryActionResource resource) {
        return new AddRecoveryActionCommand(
                resource.recoveryPlanId(),
                resource.actionType(),
                resource.notes(),
                resource.recommendedRestHours()
        );
    }
}