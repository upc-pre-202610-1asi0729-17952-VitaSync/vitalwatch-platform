package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.VitalSignReading;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendVitalSignReadingResource;

/**
 * Assembler to expose VitalSignReading using the contract expected by the Angular frontend.
 */
public final class FrontendVitalSignReadingResourceFromEntityAssembler {

    private FrontendVitalSignReadingResourceFromEntityAssembler() {
    }

    public static FrontendVitalSignReadingResource toResourceFromEntity(VitalSignReading entity) {
        return new FrontendVitalSignReadingResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getHospitalWorkspaceId(),
                entity.getUserAccountId(),
                entity.getUserAccountId(),
                entity.getHeartRateBpm(),
                entity.getHeartRateBpm(),
                entity.getSleepHoursLast24h(),
                entity.getShiftHoursLast24h(),
                entity.getSelfReportedFatigueLevel(),
                entity.getSource().name(),
                entity.getRecordedAt()
        );
    }
}