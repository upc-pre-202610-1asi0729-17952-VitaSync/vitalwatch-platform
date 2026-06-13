package com.vitalwatch.center.platform.incidents.interfaces.rest.transform;

import com.vitalwatch.center.platform.incidents.domain.model.commands.CreateIncidentCommand;
import com.vitalwatch.center.platform.incidents.interfaces.rest.resources.CreateIncidentResource;

/**
 * Assembler to convert CreateIncidentResource into CreateIncidentCommand.
 */
public final class CreateIncidentCommandFromResourceAssembler {

    private CreateIncidentCommandFromResourceAssembler() {
    }

    public static CreateIncidentCommand toCommandFromResource(CreateIncidentResource resource) {
        return new CreateIncidentCommand(
                resource.hospitalWorkspaceId(),
                resource.reportedUserAccountId(),
                resource.clinicalRiskAssessmentId(),
                resource.title(),
                resource.description(),
                resource.severity(),
                resource.source()
        );
    }
}