package com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Resource used to create an organization subscription.
 */
public record CreateSubscriptionResource(
        @NotNull
        Long organizationId,

        @NotNull
        Long planId,

        LocalDate startedAt,

        LocalDate expiresAt
) {
}