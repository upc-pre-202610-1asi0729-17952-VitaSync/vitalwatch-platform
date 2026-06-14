package com.vitalwatch.center.platform.audit.interfaces.rest.transform;

import com.vitalwatch.center.platform.audit.domain.model.aggregates.ComplianceRecord;
import com.vitalwatch.center.platform.audit.interfaces.rest.resources.ComplianceRecordResource;

/**
 * Assembler to convert ComplianceRecord aggregate into ComplianceRecordResource.
 */
public final class ComplianceRecordResourceFromEntityAssembler {

    private ComplianceRecordResourceFromEntityAssembler() {
    }

    public static ComplianceRecordResource toResourceFromEntity(ComplianceRecord entity) {
        return new ComplianceRecordResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getResourceType(),
                entity.getResourceId(),
                entity.getStatus(),
                entity.getDescription(),
                entity.getReviewedByUserAccountId(),
                entity.getReviewNotes(),
                entity.getRecordedAt(),
                entity.getReviewedAt()
        );
    }
}