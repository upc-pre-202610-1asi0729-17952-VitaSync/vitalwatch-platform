package com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.transform;

import com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.entities.ShiftRecordJpaEntity;
import com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources.ShiftRecordResource;

/**
 * Assembler used to convert shift record entities into REST resources.
 */
public final class ShiftRecordResourceFromEntityAssembler {

    private ShiftRecordResourceFromEntityAssembler() {
    }

    public static ShiftRecordResource toResourceFromEntity(ShiftRecordJpaEntity entity) {
        return new ShiftRecordResource(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getUserId(),
                entity.getWorkAreaId(),
                entity.getType().name(),
                entity.getStatus().name(),
                entity.getScheduledStart(),
                entity.getScheduledEnd(),
                entity.getCheckInAt(),
                entity.getCheckOutAt()
        );
    }
}