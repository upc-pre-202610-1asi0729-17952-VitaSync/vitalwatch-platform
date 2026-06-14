package com.vitalwatch.center.platform.shifts.interfaces.rest.transform;

import com.vitalwatch.center.platform.shifts.domain.model.commands.AssignUserToShiftCommand;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.AssignUserToShiftResource;

/**
 * Assembler to convert AssignUserToShiftResource into AssignUserToShiftCommand.
 */
public final class AssignUserToShiftCommandFromResourceAssembler {

    private AssignUserToShiftCommandFromResourceAssembler() {
    }

    public static AssignUserToShiftCommand toCommandFromResource(AssignUserToShiftResource resource) {
        return new AssignUserToShiftCommand(
                resource.workShiftId(),
                resource.userAccountId(),
                resource.assignedByUserAccountId()
        );
    }
}