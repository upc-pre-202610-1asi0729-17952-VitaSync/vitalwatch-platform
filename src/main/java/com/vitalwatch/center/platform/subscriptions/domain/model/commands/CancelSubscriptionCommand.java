package com.vitalwatch.center.platform.subscriptions.domain.model.commands;

/**
 * Command used to cancel a hospital subscription.
 */
public record CancelSubscriptionCommand(
        Long subscriptionId
) {
}