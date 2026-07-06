package com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

/**
 * Resource used to add a user to a care team.
 */
public record CreateTeamMemberResource(
        @NotNull
        Long teamId,

        @NotNull
        Long userId
) {
}