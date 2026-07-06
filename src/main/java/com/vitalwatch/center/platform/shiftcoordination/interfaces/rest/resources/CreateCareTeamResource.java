package com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Resource used to create a care team.
 */
public record CreateCareTeamResource(
        @NotNull
        Long organizationId,

        @NotBlank
        @Size(max = 120)
        String name,

        @NotNull
        Long workAreaId,

        Long supervisorId
) {
}