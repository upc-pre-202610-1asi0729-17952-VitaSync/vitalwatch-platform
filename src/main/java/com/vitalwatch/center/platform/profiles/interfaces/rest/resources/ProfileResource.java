package com.vitalwatch.center.platform.profiles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource used to expose profile data.
 */
@Schema(
        name = "ProfileResponse",
        description = "Profile information response"
)
public record ProfileResource(

        @Schema(description = "Profile unique identifier", example = "1")
        Long id,

        @Schema(description = "Profile full name", example = "John Doe")
        String fullName,

        @Schema(description = "Profile email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "Complete street address", example = "123 Main St Apt 4, Lima, 15001, Peru")
        String streetAddress
) {
}