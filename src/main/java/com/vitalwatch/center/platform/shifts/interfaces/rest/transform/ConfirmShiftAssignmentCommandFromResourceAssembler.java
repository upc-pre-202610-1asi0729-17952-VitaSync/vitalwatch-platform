package com.vitalwatch.center.platform.shifts.interfaces.rest.transform;

import com.vitalwatch.center.platform.shifts.domain.model.commands.ConfirmShiftAssignmentCommand;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.ConfirmShiftAssignmentResource;

/**
 * Assembler to convert ConfirmShiftAssignmentResource into ConfirmShiftAssignmentCommand.
 */
public final class ConfirmShiftAssignmentCommandFromResourceAssembler {

    private ConfirmShiftAssignmentCommandFromResourceAssembler() {
    }

    public static ConfirmShiftAssignmentCommand toCommandFromResource(
            Long shiftAssignmentId,
            ConfirmShiftAssignmentResource resource
    ) {
        return new ConfirmShiftAssignmentCommand(
                shiftAssignmentId,
                resource.confirmedByUserAccountId()
        );
    }
}