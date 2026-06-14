package com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.assemblers;

import com.vitalwatch.center.platform.shifts.domain.model.aggregates.WorkShift;
import com.vitalwatch.center.platform.shifts.domain.model.valueobjects.ShiftLabel;
import com.vitalwatch.center.platform.shifts.domain.model.valueobjects.WorkAreaName;
import com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.entities.WorkShiftPersistenceEntity;

/**
 * Assembler between WorkShift domain and persistence representations.
 */
public final class WorkShiftPersistenceAssembler {

    private WorkShiftPersistenceAssembler() {
    }

    public static WorkShift toDomainFromPersistence(WorkShiftPersistenceEntity entity) {
        return new WorkShift(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                new ShiftLabel(entity.getLabel()),
                new WorkAreaName(entity.getWorkArea()),
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

    public static WorkShiftPersistenceEntity toPersistenceFromDomain(WorkShift aggregate) {
        var entity = new WorkShiftPersistenceEntity();

        entity.setId(aggregate.getId());
        entity.setHospitalWorkspaceId(aggregate.getHospitalWorkspaceId());
        entity.setLabel(aggregate.getLabel());
        entity.setWorkArea(aggregate.getWorkArea());
        entity.setShiftType(aggregate.getShiftType());
        entity.setStatus(aggregate.getStatus());
        entity.setStartsAt(aggregate.getStartsAt());
        entity.setEndsAt(aggregate.getEndsAt());
        entity.setCompletedByUserAccountId(aggregate.getCompletedByUserAccountId());
        entity.setCancelledByUserAccountId(aggregate.getCancelledByUserAccountId());
        entity.setCancellationReason(aggregate.getCancellationReason());
        entity.setCompletedAt(aggregate.getCompletedAt());
        entity.setCancelledAt(aggregate.getCancelledAt());

        return entity;
    }
}