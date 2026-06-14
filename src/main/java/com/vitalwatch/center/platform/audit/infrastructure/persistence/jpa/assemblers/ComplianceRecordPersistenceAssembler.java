package com.vitalwatch.center.platform.audit.infrastructure.persistence.jpa.assemblers;

import com.vitalwatch.center.platform.audit.domain.model.aggregates.ComplianceRecord;
import com.vitalwatch.center.platform.audit.domain.model.valueobjects.AuditDescription;
import com.vitalwatch.center.platform.audit.infrastructure.persistence.jpa.entities.ComplianceRecordPersistenceEntity;

/**
 * Assembler between ComplianceRecord domain and persistence representations.
 */
public final class ComplianceRecordPersistenceAssembler {

    private ComplianceRecordPersistenceAssembler() {
    }

    public static ComplianceRecord toDomainFromPersistence(ComplianceRecordPersistenceEntity entity) {
        return new ComplianceRecord(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getResourceType(),
                entity.getResourceId(),
                entity.getStatus(),
                new AuditDescription(entity.getDescription()),
                entity.getReviewedByUserAccountId(),
                entity.getReviewNotes(),
                entity.getRecordedAt(),
                entity.getReviewedAt()
        );
    }

    public static ComplianceRecordPersistenceEntity toPersistenceFromDomain(ComplianceRecord aggregate) {
        var entity = new ComplianceRecordPersistenceEntity();

        entity.setId(aggregate.getId());
        entity.setHospitalWorkspaceId(aggregate.getHospitalWorkspaceId());
        entity.setResourceType(aggregate.getResourceType());
        entity.setResourceId(aggregate.getResourceId());
        entity.setStatus(aggregate.getStatus());
        entity.setDescription(aggregate.getDescription());
        entity.setReviewedByUserAccountId(aggregate.getReviewedByUserAccountId());
        entity.setReviewNotes(aggregate.getReviewNotes());
        entity.setRecordedAt(aggregate.getRecordedAt());
        entity.setReviewedAt(aggregate.getReviewedAt());

        return entity;
    }
}