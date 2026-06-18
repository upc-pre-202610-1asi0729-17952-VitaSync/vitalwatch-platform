package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible response for billing checkout session creation.
 */
@Schema(name = "BillingCheckoutSessionResponse", description = "Billing checkout session response compatible with the Angular frontend")
public record BillingCheckoutSessionResponseResource(
        String checkoutUrl,
        String stripeSessionId,
        Long organizationId,
        Long administratorId,
        Long subscriptionId,
        Long checkoutSessionId
) {
}