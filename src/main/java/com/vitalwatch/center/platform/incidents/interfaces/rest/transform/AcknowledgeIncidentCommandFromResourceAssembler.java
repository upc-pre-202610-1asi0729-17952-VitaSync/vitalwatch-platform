package com.vitalwatch.center.platform.incidents.interfaces.rest.transform;

import com.vitalwatch.center.platform.incidents.domain.model.commands.AcknowledgeIncidentCommand;
import com.vitalwatch.center.platform.incidents.interfaces.rest.resources.AcknowledgeIncidentResource;

/**
 * Assembler to convert AcknowledgeIncidentResource into AcknowledgeIncidentCommand.
 */
public final class AcknowledgeIncidentCommandFromResourceAssembler {

    private AcknowledgeIncidentCommandFromResourceAssembler() {
    }

    public static AcknowledgeIncidentCommand toCommandFromResource(
            Long incidentId,
            AcknowledgeIncidentResource resource
    ) {
        return new AcknowledgeIncidentCommand(
                incidentId,
                resource.acknowledgedByUserAccountId()
        );
    }
}