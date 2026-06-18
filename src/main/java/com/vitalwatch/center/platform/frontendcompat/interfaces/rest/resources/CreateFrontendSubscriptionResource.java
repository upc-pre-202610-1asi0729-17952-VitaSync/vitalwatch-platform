package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Frontend-compatible request for creating a subscription.
 */
@Schema(name = "CreateFrontendSubscriptionRequest", description = "Subscription creation request compatible with the Angular frontend")
public record CreateFrontendSubscriptionResource(
        @NotNull
        @Positive
        Long organizationId,

        @NotNull
        @Positive
        Long planId
) {
}