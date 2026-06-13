package com.vitalwatch.center.platform.subscriptions.domain.model.commands;

/**
 * Command used to subscribe a hospital workspace to a plan.
 */
public record SubscribeHospitalWorkspaceCommand(
        Long hospitalWorkspaceId,
        Long subscriptionPlanId
) {
}