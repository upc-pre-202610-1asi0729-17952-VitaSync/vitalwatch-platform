package com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.transform;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.RegisterVitalSignReadingCommand;
import com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.resources.RegisterVitalSignReadingResource;

/**
 * Assembler to convert RegisterVitalSignReadingResource into RegisterVitalSignReadingCommand.
 */
public final class RegisterVitalSignReadingCommandFromResourceAssembler {

    private RegisterVitalSignReadingCommandFromResourceAssembler() {
    }

    public static RegisterVitalSignReadingCommand toCommandFromResource(RegisterVitalSignReadingResource resource) {
        return new RegisterVitalSignReadingCommand(
                resource.hospitalWorkspaceId(),
                resource.userAccountId(),
                resource.heartRateBpm(),
                resource.sleepHoursLast24h(),
                resource.shiftHoursLast24h(),
                resource.selfReportedFatigueLevel(),
                resource.source()
        );
    }
}