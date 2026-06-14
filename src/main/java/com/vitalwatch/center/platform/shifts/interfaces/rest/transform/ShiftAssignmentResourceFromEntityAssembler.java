package com.vitalwatch.center.platform.shifts.interfaces.rest.transform;

import com.vitalwatch.center.platform.shifts.domain.model.aggregates.ShiftAssignment;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.ShiftAssignmentResource;

/**
 * Assembler to convert ShiftAssignment aggregate into ShiftAssignmentResource.
 */
public final class ShiftAssignmentResourceFromEntityAssembler {

    private ShiftAssignmentResourceFromEntityAssembler() {
    }

    public static ShiftAssignmentResource toResourceFromEntity(ShiftAssignment entity) {
        return new ShiftAssignmentResource(
                entity.getId(),
                entity.getWorkShiftId(),
                entity.getUserAccountId(),
                entity.getAssignedByUserAccountId(),
                entity.getConfirmedByUserAccountId(),
                entity.getReleasedByUserAccountId(),
                entity.getStatus(),
                entity.getReleaseReason(),
                entity.getAssignedAt(),
                entity.getConfirmedAt(),
                entity.getReleasedAt()
        );
    }
}