package com.vitalwatch.center.platform.incidents.domain.model.commands;

/**
 * Command used to resolve an incident.
 */
public record ResolveIncidentCommand(
        Long incidentId,
        Long resolvedByUserAccountId,
        String resolutionNotes
) {
}