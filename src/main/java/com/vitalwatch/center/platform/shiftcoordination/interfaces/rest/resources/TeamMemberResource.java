package com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources;

/**
 * Resource used to expose team member data.
 */
public record TeamMemberResource(
        Long id,
        Long teamId,
        Long userId
) {
}