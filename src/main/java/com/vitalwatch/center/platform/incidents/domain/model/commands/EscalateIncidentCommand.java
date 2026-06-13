package com.vitalwatch.center.platform.incidents.domain.model.commands;

import com.vitalwatch.center.platform.incidents.domain.model.enums.EscalationLevel;

/**
 * Command used to escalate an incident.
 */
public record EscalateIncidentCommand(
        Long incidentId,
        Long escalatedByUserAccountId,
        EscalationLevel escalationLevel
) {
}