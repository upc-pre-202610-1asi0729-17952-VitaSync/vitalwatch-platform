package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform;

import com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.entities.PreventiveActionJpaEntity;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.PreventiveActionResource;

/**
 * Assembler used to convert preventive action entities into REST resources.
 */
public final class PreventiveActionResourceFromEntityAssembler {

    private PreventiveActionResourceFromEntityAssembler() {
    }

    public static PreventiveActionResource toResourceFromEntity(PreventiveActionJpaEntity entity) {
        return new PreventiveActionResource(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getSupervisorId(),
                entity.getUserId(),
                entity.getType().name(),
                entity.getStatus().name(),
                entity.getNotes(),
                entity.getCreatedAt(),
                entity.getCompletedAt()
        );
    }
}