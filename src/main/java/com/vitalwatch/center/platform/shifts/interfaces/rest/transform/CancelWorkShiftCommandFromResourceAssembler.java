package com.vitalwatch.center.platform.shifts.interfaces.rest.transform;

import com.vitalwatch.center.platform.shifts.domain.model.commands.CancelWorkShiftCommand;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.CancelWorkShiftResource;

/**
 * Assembler to convert CancelWorkShiftResource into CancelWorkShiftCommand.
 */
public final class CancelWorkShiftCommandFromResourceAssembler {

    private CancelWorkShiftCommandFromResourceAssembler() {
    }

    public static CancelWorkShiftCommand toCommandFromResource(
            Long workShiftId,
            CancelWorkShiftResource resource
    ) {
        return new CancelWorkShiftCommand(
                workShiftId,
                resource.cancelledByUserAccountId(),
                resource.cancellationReason()
        );
    }
}