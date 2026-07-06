package com.vitalwatch.center.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Resource used to create a hospital organization.
 */
public record CreateOrganizationResource(
        @NotBlank
        @Size(max = 150)
        String legalName,

        @NotBlank
        @Size(max = 150)
        String commercialName,

        @NotBlank
        @Pattern(regexp = "\\d{11}", message = "RUC must contain exactly 11 digits")
        String ruc,

        @NotBlank
        @Email
        @Size(max = 120)
        String email,

        @Size(max = 30)
        String phone,

        @Size(max = 200)
        String address
) {
}