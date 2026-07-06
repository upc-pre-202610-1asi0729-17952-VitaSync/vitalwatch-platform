package com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.transform;

import com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.entities.TeamMemberJpaEntity;
import com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources.TeamMemberResource;

/**
 * Assembler used to convert team member entities into REST resources.
 */
public final class TeamMemberResourceFromEntityAssembler {

    private TeamMemberResourceFromEntityAssembler() {
    }

    public static TeamMemberResource toResourceFromEntity(TeamMemberJpaEntity entity) {
        return new TeamMemberResource(
                entity.getId(),
                entity.getTeamId(),
                entity.getUserId()
        );
    }
}