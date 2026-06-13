package com.vitalwatch.center.platform.incidents.interfaces.rest.transform;

import com.vitalwatch.center.platform.incidents.domain.model.commands.EscalateIncidentCommand;
import com.vitalwatch.center.platform.incidents.interfaces.rest.resources.EscalateIncidentResource;

/**
 * Assembler to convert EscalateIncidentResource into EscalateIncidentCommand.
 */
public final class EscalateIncidentCommandFromResourceAssembler {

    private EscalateIncidentCommandFromResourceAssembler() {
    }

    public static EscalateIncidentCommand toCommandFromResource(
            Long incidentId,
            EscalateIncidentResource resource
    ) {
        return new EscalateIncidentCommand(
                incidentId,
                resource.escalatedByUserAccountId(),
                resource.escalationLevel()
        );
    }
}