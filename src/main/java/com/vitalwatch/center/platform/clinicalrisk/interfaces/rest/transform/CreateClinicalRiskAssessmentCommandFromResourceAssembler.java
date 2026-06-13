package com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.transform;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.CreateClinicalRiskAssessmentCommand;
import com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.resources.CreateClinicalRiskAssessmentResource;

/**
 * Assembler to convert CreateClinicalRiskAssessmentResource into CreateClinicalRiskAssessmentCommand.
 */
public final class CreateClinicalRiskAssessmentCommandFromResourceAssembler {

    private CreateClinicalRiskAssessmentCommandFromResourceAssembler() {
    }

    public static CreateClinicalRiskAssessmentCommand toCommandFromResource(CreateClinicalRiskAssessmentResource resource) {
        return new CreateClinicalRiskAssessmentCommand(
                resource.hospitalWorkspaceId(),
                resource.userAccountId(),
                resource.vitalSignReadingId()
        );
    }
}