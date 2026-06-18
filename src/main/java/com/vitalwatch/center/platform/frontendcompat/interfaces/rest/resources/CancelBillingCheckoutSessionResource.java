package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for cancelling checkout session.
 */
@Schema(name = "CancelBillingCheckoutSessionRequest", description = "Checkout cancellation request compatible with the Angular frontend")
public record CancelBillingCheckoutSessionResource(
        Long checkoutSessionId
) {
}