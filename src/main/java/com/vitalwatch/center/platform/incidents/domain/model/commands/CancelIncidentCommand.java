package com.vitalwatch.center.platform.incidents.domain.model.commands;

/**
 * Command used to cancel an incident.
 */
public record CancelIncidentCommand(
        Long incidentId,
        Long cancelledByUserAccountId,
        String cancellationReason
) {
}