package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible response for checkout sessions.
 */
@Schema(name = "FrontendCheckoutSessionResponse", description = "Checkout session response compatible with the Angular frontend")
public record FrontendCheckoutSessionResource(
        Long id,
        Long organizationId,
        Long hospitalWorkspaceId,
        Long administratorId,
        Long administratorUserAccountId,
        Long subscriptionId,
        Long planId,
        Long subscriptionPlanId,
        String planCode,
        String status,
        Instant createdAt
) {
}