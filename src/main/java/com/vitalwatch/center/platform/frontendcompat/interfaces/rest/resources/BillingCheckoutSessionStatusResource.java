package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible response for checkout status.
 */
@Schema(name = "BillingCheckoutSessionStatusResponse", description = "Checkout session status response compatible with the Angular frontend")
public record BillingCheckoutSessionStatusResource(
        String stripeSessionId,
        String status,
        String paymentStatus,
        Boolean activated
) {
}