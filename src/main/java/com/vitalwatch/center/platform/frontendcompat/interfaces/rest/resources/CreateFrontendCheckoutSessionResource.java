package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for creating checkout sessions.
 */
@Schema(name = "CreateFrontendCheckoutSessionRequest", description = "Checkout session creation request compatible with the Angular frontend")
public record CreateFrontendCheckoutSessionResource(
        Long organizationId,
        Long hospitalWorkspaceId,
        Long administratorId,
        Long administratorUserAccountId,
        Long subscriptionId,
        Long planId,
        Long subscriptionPlanId,
        String planCode,
        String status,
        String createdAt
) {
}