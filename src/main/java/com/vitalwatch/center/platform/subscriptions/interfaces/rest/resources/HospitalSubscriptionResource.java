package com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources;

import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource used to expose hospital subscription data.
 */
@Schema(name = "HospitalSubscriptionResponse", description = "Hospital subscription information response")
public record HospitalSubscriptionResource(

        @Schema(description = "Hospital subscription id", example = "1")
        Long id,

        @Schema(description = "Hospital workspace id", example = "1")
        Long hospitalWorkspaceId,

        @Schema(description = "Subscription plan id", example = "1")
        Long subscriptionPlanId,

        @Schema(description = "Subscription status", example = "ACTIVE")
        SubscriptionStatus status,

        @Schema(description = "Subscription start date")
        Instant startedAt,

        @Schema(description = "Subscription cancellation date")
        Instant cancelledAt,

        @Schema(description = "Subscription expiration date")
        Instant expiresAt
) {
}