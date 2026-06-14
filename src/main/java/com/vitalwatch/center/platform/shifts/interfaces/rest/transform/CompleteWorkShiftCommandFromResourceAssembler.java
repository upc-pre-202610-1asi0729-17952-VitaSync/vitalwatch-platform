package com.vitalwatch.center.platform.shifts.interfaces.rest.transform;

import com.vitalwatch.center.platform.shifts.domain.model.commands.CompleteWorkShiftCommand;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.CompleteWorkShiftResource;

/**
 * Assembler to convert CompleteWorkShiftResource into CompleteWorkShiftCommand.
 */
public final class CompleteWorkShiftCommandFromResourceAssembler {

    private CompleteWorkShiftCommandFromResourceAssembler() {
    }

    public static CompleteWorkShiftCommand toCommandFromResource(
            Long workShiftId,
            CompleteWorkShiftResource resource
    ) {
        return new CompleteWorkShiftCommand(
                workShiftId,
                resource.completedByUserAccountId()
        );
    }
}