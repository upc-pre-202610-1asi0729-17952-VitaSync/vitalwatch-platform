package com.vitalwatch.center.platform.iam.catalogs.interfaces.rest.transform;

import com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.entities.SpecialtyJpaEntity;
import com.vitalwatch.center.platform.iam.catalogs.interfaces.rest.resources.SpecialtyResource;

/**
 * Assembler used to convert specialty entities into REST resources.
 */
public final class SpecialtyResourceFromEntityAssembler {

    private SpecialtyResourceFromEntityAssembler() {
    }

    public static SpecialtyResource toResourceFromEntity(SpecialtyJpaEntity entity) {
        return new SpecialtyResource(
                entity.getId(),
                entity.getName()
        );
    }
}