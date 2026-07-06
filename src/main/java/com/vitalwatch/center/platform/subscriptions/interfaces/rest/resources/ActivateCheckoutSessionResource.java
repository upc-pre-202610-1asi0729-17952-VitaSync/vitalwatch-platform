package com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

/**
 * Resource used to activate an organization subscription from a checkout session.
 */
public record ActivateCheckoutSessionResource(
        String sessionId,

        String stripeSessionId,

        @NotNull
        Long organizationId
) {
}