package com.vitalwatch.center.platform.clinicalrisk.domain.model.commands;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.enums.VitalSignSource;

/**
 * Command used to register staff physiological and workload data.
 */
public record RegisterVitalSignReadingCommand(
        Long hospitalWorkspaceId,
        Long userAccountId,
        Integer heartRateBpm,
        Double sleepHoursLast24h,
        Double shiftHoursLast24h,
        Integer selfReportedFatigueLevel,
        VitalSignSource source
) {
}