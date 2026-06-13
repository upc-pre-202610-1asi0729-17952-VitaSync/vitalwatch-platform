package com.vitalwatch.center.platform.profiles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Resource used to create a profile.
 */
@Schema(
        name = "CreateProfileRequest",
        description = "Request payload for creating a new profile"
)
public record CreateProfileResource(

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Profile first name", example = "John")
        String firstName,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Profile last name", example = "Doe")
        String lastName,

        @NotBlank(message = "{validation.not-blank}")
        @Email(message = "{validation.email}")
        @Schema(description = "Profile email address", example = "john.doe@example.com")
        String email,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Street address", example = "123 Main St")
        String street,

        @Schema(description = "Street number or apartment", example = "Apt 4")
        String number,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "City name", example = "Lima")
        String city,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Postal code", example = "15001")
        String postalCode,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Country name", example = "Peru")
        String country
) {
}