package com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources;

import java.time.LocalDate;

/**
 * Resource used to expose subscription data through the REST API.
 */
public record SubscriptionResource(
        Long id,
        Long organizationId,
        String organizationName,
        Long planId,
        String planCode,
        String planName,
        String status,
        LocalDate startedAt,
        LocalDate expiresAt,
        String stripeCustomerId,
        String stripeSubscriptionId
) {
}