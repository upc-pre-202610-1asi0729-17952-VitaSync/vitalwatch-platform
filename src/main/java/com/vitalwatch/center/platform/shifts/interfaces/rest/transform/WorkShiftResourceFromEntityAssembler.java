package com.vitalwatch.center.platform.shifts.interfaces.rest.transform;

import com.vitalwatch.center.platform.shifts.domain.model.aggregates.WorkShift;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.WorkShiftResource;

/**
 * Assembler to convert WorkShift aggregate into WorkShiftResource.
 */
public final class WorkShiftResourceFromEntityAssembler {

    private WorkShiftResourceFromEntityAssembler() {
    }

    public static WorkShiftResource toResourceFromEntity(WorkShift entity) {
        return new WorkShiftResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getLabel(),
                entity.getWorkArea(),
                entity.getShiftType(),
                entity.getStatus(),
                entity.getStartsAt(),
                entity.getEndsAt(),
                entity.getCompletedByUserAccountId(),
                entity.getCancelledByUserAccountId(),
                entity.getCancellationReason(),
                entity.getCompletedAt(),
                entity.getCancelledAt()
        );
    }
}