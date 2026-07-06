package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.transform;

import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.VitalSignReadingJpaEntity;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources.VitalSignReadingResource;

/**
 * Assembler used to convert vital sign reading entities into REST resources.
 */
public final class VitalSignReadingResourceFromEntityAssembler {

    private VitalSignReadingResourceFromEntityAssembler() {
    }

    public static VitalSignReadingResource toResourceFromEntity(VitalSignReadingJpaEntity entity) {
        return new VitalSignReadingResource(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getUserId(),
                entity.getHeartRate(),
                entity.getHrv(),
                entity.getFatigueLevel(),
                entity.getCortisolLevel(),
                entity.getSensorStatus().name(),
                entity.getRecordedAt()
        );
    }
}