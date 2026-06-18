package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Frontend-compatible request for organization checkout registration.
 */
@Schema(name = "CreateBillingCheckoutSessionRequest", description = "Billing checkout request compatible with the Angular frontend")
public record CreateBillingCheckoutSessionResource(
        String planCode,
        BillingOrganizationResource organization,
        BillingAdministratorResource administrator
) {
    public record BillingOrganizationResource(
            String name,
            String ruc,
            String address,
            String phone
    ) {
    }

    public record BillingAdministratorResource(
            String firstName,
            String lastName,
            String email,
            String password,
            String phone
    ) {
    }
}