package com.vitalwatch.center.platform.incidents.interfaces.rest.transform;

import com.vitalwatch.center.platform.incidents.domain.model.commands.ResolveIncidentCommand;
import com.vitalwatch.center.platform.incidents.interfaces.rest.resources.ResolveIncidentResource;

/**
 * Assembler to convert ResolveIncidentResource into ResolveIncidentCommand.
 */
public final class ResolveIncidentCommandFromResourceAssembler {

    private ResolveIncidentCommandFromResourceAssembler() {
    }

    public static ResolveIncidentCommand toCommandFromResource(
            Long incidentId,
            ResolveIncidentResource resource
    ) {
        return new ResolveIncidentCommand(
                incidentId,
                resource.resolvedByUserAccountId(),
                resource.resolutionNotes()
        );
    }
}