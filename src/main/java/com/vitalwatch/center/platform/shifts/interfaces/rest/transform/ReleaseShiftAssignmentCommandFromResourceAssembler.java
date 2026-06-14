package com.vitalwatch.center.platform.shifts.interfaces.rest.transform;

import com.vitalwatch.center.platform.shifts.domain.model.commands.ReleaseShiftAssignmentCommand;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.ReleaseShiftAssignmentResource;

/**
 * Assembler to convert ReleaseShiftAssignmentResource into ReleaseShiftAssignmentCommand.
 */
public final class ReleaseShiftAssignmentCommandFromResourceAssembler {

    private ReleaseShiftAssignmentCommandFromResourceAssembler() {
    }

    public static ReleaseShiftAssignmentCommand toCommandFromResource(
            Long shiftAssignmentId,
            ReleaseShiftAssignmentResource resource
    ) {
        return new ReleaseShiftAssignmentCommand(
                shiftAssignmentId,
                resource.releasedByUserAccountId(),
                resource.releaseReason()
        );
    }
}