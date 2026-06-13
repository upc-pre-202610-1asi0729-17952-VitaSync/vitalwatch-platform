package com.vitalwatch.center.platform.incidents.domain.model.commands;

import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentSeverity;
import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentSource;

/**
 * Command used to create an incident from a clinical risk situation or manual report.
 */
public record CreateIncidentCommand(
        Long hospitalWorkspaceId,
        Long reportedUserAccountId,
        Long clinicalRiskAssessmentId,
        String title,
        String description,
        IncidentSeverity severity,
        IncidentSource source
) {
}