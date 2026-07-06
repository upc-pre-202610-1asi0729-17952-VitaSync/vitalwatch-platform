package com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

/**
 * Resource used to update an existing subscription.
 */
public record UpdateSubscriptionResource(
        @NotNull
        Long planId
) {
}