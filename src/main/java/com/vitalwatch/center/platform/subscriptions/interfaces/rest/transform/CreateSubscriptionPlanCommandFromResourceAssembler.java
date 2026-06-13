package com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform;

import com.vitalwatch.center.platform.subscriptions.domain.model.commands.CreateSubscriptionPlanCommand;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.CreateSubscriptionPlanResource;

/**
 * Assembler to convert CreateSubscriptionPlanResource into CreateSubscriptionPlanCommand.
 */
public final class CreateSubscriptionPlanCommandFromResourceAssembler {

    private CreateSubscriptionPlanCommandFromResourceAssembler() {
    }

    public static CreateSubscriptionPlanCommand toCommandFromResource(CreateSubscriptionPlanResource resource) {
        return new CreateSubscriptionPlanCommand(
                resource.code(),
                resource.name(),
                resource.description(),
                resource.priceAmount(),
                resource.currency(),
                resource.billingPeriod(),
                resource.maxDoctors(),
                resource.maxSupervisors(),
                resource.maxTeams(),
                resource.maxWorkAreas(),
                resource.monthlyInvitations(),
                resource.dataHistoryDays(),
                resource.supportLevel()
        );
    }
}