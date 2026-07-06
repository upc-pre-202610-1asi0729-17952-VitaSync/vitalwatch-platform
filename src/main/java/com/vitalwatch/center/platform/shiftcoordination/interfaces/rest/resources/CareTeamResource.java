package com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources;

/**
 * Resource used to expose care team data.
 */
public record CareTeamResource(
        Long id,
        Long organizationId,
        String name,
        Long workAreaId,
        Long supervisorId,
        String status
) {
}