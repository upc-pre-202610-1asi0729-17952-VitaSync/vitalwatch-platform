package com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform;

import com.vitalwatch.center.platform.subscriptions.domain.model.commands.SubscribeHospitalWorkspaceCommand;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.SubscribeHospitalWorkspaceResource;

/**
 * Assembler to convert SubscribeHospitalWorkspaceResource into SubscribeHospitalWorkspaceCommand.
 */
public final class SubscribeHospitalWorkspaceCommandFromResourceAssembler {

    private SubscribeHospitalWorkspaceCommandFromResourceAssembler() {
    }

    public static SubscribeHospitalWorkspaceCommand toCommandFromResource(SubscribeHospitalWorkspaceResource resource) {
        return new SubscribeHospitalWorkspaceCommand(
                resource.hospitalWorkspaceId(),
                resource.subscriptionPlanId()
        );
    }
}