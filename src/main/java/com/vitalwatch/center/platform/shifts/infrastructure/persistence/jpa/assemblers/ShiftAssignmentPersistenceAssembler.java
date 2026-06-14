package com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.assemblers;

import com.vitalwatch.center.platform.shifts.domain.model.aggregates.ShiftAssignment;
import com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.entities.ShiftAssignmentPersistenceEntity;

/**
 * Assembler between ShiftAssignment domain and persistence representations.
 */
public final class ShiftAssignmentPersistenceAssembler {

    private ShiftAssignmentPersistenceAssembler() {
    }

    public static ShiftAssignment toDomainFromPersistence(ShiftAssignmentPersistenceEntity entity) {
        return new ShiftAssignment(
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

    public static ShiftAssignmentPersistenceEntity toPersistenceFromDomain(ShiftAssignment aggregate) {
        var entity = new ShiftAssignmentPersistenceEntity();

        entity.setId(aggregate.getId());
        entity.setWorkShiftId(aggregate.getWorkShiftId());
        entity.setUserAccountId(aggregate.getUserAccountId());
        entity.setAssignedByUserAccountId(aggregate.getAssignedByUserAccountId());
        entity.setConfirmedByUserAccountId(aggregate.getConfirmedByUserAccountId());
        entity.setReleasedByUserAccountId(aggregate.getReleasedByUserAccountId());
        entity.setStatus(aggregate.getStatus());
        entity.setReleaseReason(aggregate.getReleaseReason());
        entity.setAssignedAt(aggregate.getAssignedAt());
        entity.setConfirmedAt(aggregate.getConfirmedAt());
        entity.setReleasedAt(aggregate.getReleasedAt());

        return entity;
    }
}