package com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Resource used to create a checkout session.
 */
public record CreateCheckoutSessionResource(
        Long organizationId,

        Long planId,

        String planCode,

        @NotBlank
        @Size(max = 500)
        String successUrl,

        @NotBlank
        @Size(max = 500)
        String cancelUrl
) {
}