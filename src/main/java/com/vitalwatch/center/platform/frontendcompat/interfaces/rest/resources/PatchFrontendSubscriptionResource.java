package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for updating a subscription.
 */
@Schema(name = "PatchFrontendSubscriptionRequest", description = "Subscription update request compatible with the Angular frontend")
public record PatchFrontendSubscriptionResource(
        Long planId,
        Long subscriptionPlanId,
        String status
) {
}