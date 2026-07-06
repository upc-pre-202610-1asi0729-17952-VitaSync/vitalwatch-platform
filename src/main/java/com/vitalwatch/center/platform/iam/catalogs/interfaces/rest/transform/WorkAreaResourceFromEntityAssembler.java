package com.vitalwatch.center.platform.iam.catalogs.interfaces.rest.transform;

import com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.entities.WorkAreaJpaEntity;
import com.vitalwatch.center.platform.iam.catalogs.interfaces.rest.resources.WorkAreaResource;

/**
 * Assembler used to convert work area entities into REST resources.
 */
public final class WorkAreaResourceFromEntityAssembler {

    private WorkAreaResourceFromEntityAssembler() {
    }

    public static WorkAreaResource toResourceFromEntity(WorkAreaJpaEntity entity) {
        return new WorkAreaResource(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getName()
        );
    }
}