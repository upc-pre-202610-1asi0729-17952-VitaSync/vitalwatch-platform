package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Frontend-compatible response for hospital subscriptions.
 */
@Schema(name = "FrontendSubscriptionResponse", description = "Subscription response compatible with the Angular frontend")
public record FrontendSubscriptionResource(
        Long id,
        Long organizationId,
        Long hospitalWorkspaceId,
        Long planId,
        Long subscriptionPlanId,
        String status,
        Instant startDate,
        Instant startedAt,
        Instant cancelledAt,
        Instant expiresAt
) {
}