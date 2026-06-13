package com.vitalwatch.center.platform.subscriptions.domain.model.commands;

/**
 * Command used to change the plan of an active subscription.
 */
public record ChangeSubscriptionPlanCommand(
        Long subscriptionId,
        Long newSubscriptionPlanId
) {
}