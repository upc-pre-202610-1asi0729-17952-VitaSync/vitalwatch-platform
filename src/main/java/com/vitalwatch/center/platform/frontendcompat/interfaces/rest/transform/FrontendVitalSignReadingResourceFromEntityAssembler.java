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
        var heartRate = entity.getHeartRateBpm();
        var fatigueLevel = entity.getSelfReportedFatigueLevel();
        var hrv = Math.max(20, 90 - fatigueLevel * 8);
        var cortisol = 10.0 + fatigueLevel * 2.5;

        return new FrontendVitalSignReadingResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getHospitalWorkspaceId(),
                entity.getUserAccountId(),
                entity.getUserAccountId(),
                heartRate,
                heartRate,
                heartRate,
                hrv,
                fatigueLevel,
                cortisol,
                "ONLINE",
                entity.getSleepHoursLast24h(),
                entity.getShiftHoursLast24h(),
                fatigueLevel,
                entity.getSource().name(),
                entity.getRecordedAt()
        );
    }
}