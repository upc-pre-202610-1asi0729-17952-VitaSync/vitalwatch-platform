package com.vitalwatch.center.platform.shifts.interfaces.rest.transform;

import com.vitalwatch.center.platform.shifts.domain.model.commands.CreateWorkShiftCommand;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.CreateWorkShiftResource;

/**
 * Assembler to convert CreateWorkShiftResource into CreateWorkShiftCommand.
 */
public final class CreateWorkShiftCommandFromResourceAssembler {

    private CreateWorkShiftCommandFromResourceAssembler() {
    }

    public static CreateWorkShiftCommand toCommandFromResource(CreateWorkShiftResource resource) {
        return new CreateWorkShiftCommand(
                resource.hospitalWorkspaceId(),
                resource.label(),
                resource.workArea(),
                resource.shiftType(),
                resource.startsAt(),
                resource.endsAt()
        );
    }
}