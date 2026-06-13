package com.vitalwatch.center.platform.iam.interfaces.rest.transform;

import com.vitalwatch.center.platform.iam.domain.model.commands.CreateHospitalWorkspaceCommand;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.CreateHospitalWorkspaceResource;

/**
 * Assembler to convert CreateHospitalWorkspaceResource into CreateHospitalWorkspaceCommand.
 */
public final class CreateHospitalWorkspaceCommandFromResourceAssembler {

    private CreateHospitalWorkspaceCommandFromResourceAssembler() {
    }

    public static CreateHospitalWorkspaceCommand toCommandFromResource(CreateHospitalWorkspaceResource resource) {
        return new CreateHospitalWorkspaceCommand(
                resource.name(),
                resource.ruc(),
                resource.administratorProfileId(),
                resource.administratorEmail()
        );
    }
}