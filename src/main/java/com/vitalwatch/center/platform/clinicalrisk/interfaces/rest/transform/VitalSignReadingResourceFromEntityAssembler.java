package com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.transform;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.VitalSignReading;
import com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.resources.VitalSignReadingResource;

/**
 * Assembler to convert VitalSignReading aggregate into VitalSignReadingResource.
 */
public final class VitalSignReadingResourceFromEntityAssembler {

    private VitalSignReadingResourceFromEntityAssembler() {
    }

    public static VitalSignReadingResource toResourceFromEntity(VitalSignReading entity) {
        return new VitalSignReadingResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getUserAccountId(),
                entity.getHeartRateBpm(),
                entity.getSleepHoursLast24h(),
                entity.getShiftHoursLast24h(),
                entity.getSelfReportedFatigueLevel(),
                entity.getSource(),
                entity.getRecordedAt()
        );
    }
}