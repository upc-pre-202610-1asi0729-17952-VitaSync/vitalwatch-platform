package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendShiftAssignmentResource;
import com.vitalwatch.center.platform.shifts.domain.model.aggregates.ShiftAssignment;

/**
 * Assembler to expose ShiftAssignment using the contract expected by the Angular frontend.
 */
public final class FrontendShiftAssignmentResourceFromEntityAssembler {

    private FrontendShiftAssignmentResourceFromEntityAssembler() {
    }

    public static FrontendShiftAssignmentResource toResourceFromEntity(ShiftAssignment entity) {
        return new FrontendShiftAssignmentResource(
                entity.getId(),
                entity.getWorkShiftId(),
                entity.getWorkShiftId(),
                entity.getUserAccountId(),
                entity.getUserAccountId(),
                entity.getAssignedByUserAccountId(),
                entity.getConfirmedByUserAccountId(),
                entity.getReleasedByUserAccountId(),
                entity.getStatus().name(),
                entity.getReleaseReason(),
                entity.getAssignedAt(),
                entity.getConfirmedAt(),
                entity.getReleasedAt()
        );
    }
}