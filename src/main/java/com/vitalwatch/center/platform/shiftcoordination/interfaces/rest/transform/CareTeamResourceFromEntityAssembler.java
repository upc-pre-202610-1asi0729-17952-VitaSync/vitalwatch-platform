package com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.transform;

import com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.entities.CareTeamJpaEntity;
import com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources.CareTeamResource;

/**
 * Assembler used to convert care team entities into REST resources.
 */
public final class CareTeamResourceFromEntityAssembler {

    private CareTeamResourceFromEntityAssembler() {
    }

    public static CareTeamResource toResourceFromEntity(CareTeamJpaEntity entity) {
        return new CareTeamResource(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getName(),
                entity.getWorkAreaId(),
                entity.getSupervisorId(),
                entity.getStatus().name()
        );
    }
}