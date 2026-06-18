package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendShiftRecordResource;
import com.vitalwatch.center.platform.shifts.domain.model.aggregates.WorkShift;

/**
 * Assembler to expose WorkShift using the contract expected by the Angular frontend.
 */
public final class FrontendShiftRecordResourceFromEntityAssembler {

    private FrontendShiftRecordResourceFromEntityAssembler() {
    }

    public static FrontendShiftRecordResource toResourceFromEntity(WorkShift entity) {
        return new FrontendShiftRecordResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getHospitalWorkspaceId(),
                entity.getLabel(),
                entity.getLabel(),
                entity.getWorkArea(),
                entity.getShiftType().name(),
                entity.getShiftType().name(),
                entity.getStatus().name(),
                entity.getStartsAt(),
                entity.getStartsAt(),
                entity.getEndsAt(),
                entity.getEndsAt(),
                entity.getCompletedByUserAccountId(),
                entity.getCancelledByUserAccountId(),
                entity.getCancellationReason(),
                entity.getCompletedAt(),
                entity.getCancelledAt()
        );
    }
}