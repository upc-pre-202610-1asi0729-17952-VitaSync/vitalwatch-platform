package com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources;

import com.vitalwatch.center.platform.shiftcoordination.domain.model.enums.CareTeamStatus;

/**
 * Resource used to partially update a care team.
 */
public record UpdateCareTeamResource(
        String name,
        Long workAreaId,
        Long supervisorId,
        CareTeamStatus status
) {
}