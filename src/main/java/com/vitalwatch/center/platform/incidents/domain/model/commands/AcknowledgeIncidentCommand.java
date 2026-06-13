package com.vitalwatch.center.platform.incidents.domain.model.commands;

/**
 * Command used to acknowledge an open incident.
 */
public record AcknowledgeIncidentCommand(
        Long incidentId,
        Long acknowledgedByUserAccountId
) {
}