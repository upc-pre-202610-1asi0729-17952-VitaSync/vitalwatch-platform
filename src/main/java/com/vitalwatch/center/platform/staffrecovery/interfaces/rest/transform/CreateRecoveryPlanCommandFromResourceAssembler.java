package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform;

import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CreateRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.CreateRecoveryPlanResource;

/**
 * Assembler to convert CreateRecoveryPlanResource into CreateRecoveryPlanCommand.
 */
public final class CreateRecoveryPlanCommandFromResourceAssembler {

    private CreateRecoveryPlanCommandFromResourceAssembler() {
    }

    public static CreateRecoveryPlanCommand toCommandFromResource(CreateRecoveryPlanResource resource) {
        return new CreateRecoveryPlanCommand(
                resource.hospitalWorkspaceId(),
                resource.userAccountId(),
                resource.clinicalRiskAssessmentId(),
                resource.incidentId(),
                resource.reason(),
                resource.priority(),
                resource.recommendedRestHours(),
                resource.notes()
        );
    }
}