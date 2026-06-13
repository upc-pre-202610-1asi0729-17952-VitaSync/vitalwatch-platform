package com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform;

import com.vitalwatch.center.platform.subscriptions.domain.model.commands.ChangeSubscriptionPlanCommand;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.ChangeSubscriptionPlanResource;

/**
 * Assembler to convert ChangeSubscriptionPlanResource into ChangeSubscriptionPlanCommand.
 */
public final class ChangeSubscriptionPlanCommandFromResourceAssembler {

    private ChangeSubscriptionPlanCommandFromResourceAssembler() {
    }

    public static ChangeSubscriptionPlanCommand toCommandFromResource(
            Long subscriptionId,
            ChangeSubscriptionPlanResource resource
    ) {
        return new ChangeSubscriptionPlanCommand(subscriptionId, resource.newSubscriptionPlanId());
    }
}