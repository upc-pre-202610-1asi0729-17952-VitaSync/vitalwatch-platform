package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible response for checkout cancellation.
 */
@Schema(name = "CancelBillingCheckoutSessionResponse", description = "Checkout cancellation response compatible with the Angular frontend")
public record CancelBillingCheckoutSessionResponseResource(
        Boolean cancelled,
        Long checkoutSessionId
) {
}