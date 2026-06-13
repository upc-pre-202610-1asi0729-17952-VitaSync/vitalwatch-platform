package com.vitalwatch.center.platform.incidents.interfaces.rest.transform;

import com.vitalwatch.center.platform.incidents.domain.model.commands.CancelIncidentCommand;
import com.vitalwatch.center.platform.incidents.interfaces.rest.resources.CancelIncidentResource;

/**
 * Assembler to convert CancelIncidentResource into CancelIncidentCommand.
 */
public final class CancelIncidentCommandFromResourceAssembler {

    private CancelIncidentCommandFromResourceAssembler() {
    }

    public static CancelIncidentCommand toCommandFromResource(
            Long incidentId,
            CancelIncidentResource resource
    ) {
        return new CancelIncidentCommand(
                incidentId,
                resource.cancelledByUserAccountId(),
                resource.cancellationReason()
        );
    }
}