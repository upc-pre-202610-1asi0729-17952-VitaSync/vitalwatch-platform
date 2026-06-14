package com.vitalwatch.center.platform.audit.infrastructure.persistence.jpa.assemblers;

import com.vitalwatch.center.platform.audit.domain.model.aggregates.AuditLog;
import com.vitalwatch.center.platform.audit.domain.model.valueobjects.AuditDescription;
import com.vitalwatch.center.platform.audit.domain.model.valueobjects.IpAddress;
import com.vitalwatch.center.platform.audit.infrastructure.persistence.jpa.entities.AuditLogPersistenceEntity;

/**
 * Assembler between AuditLog domain and persistence representations.
 */
public final class AuditLogPersistenceAssembler {

    private AuditLogPersistenceAssembler() {
    }

    public static AuditLog toDomainFromPersistence(AuditLogPersistenceEntity entity) {
        return new AuditLog(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getActorUserAccountId(),
                entity.getActionType(),
                entity.getResourceType(),
                entity.getResourceId(),
                entity.getSeverity(),
                new AuditDescription(entity.getDescription()),
                new IpAddress(entity.getIpAddress()),
                entity.getOccurredAt()
        );
    }

    public static AuditLogPersistenceEntity toPersistenceFromDomain(AuditLog aggregate) {
        var entity = new AuditLogPersistenceEntity();

        entity.setId(aggregate.getId());
        entity.setHospitalWorkspaceId(aggregate.getHospitalWorkspaceId());
        entity.setActorUserAccountId(aggregate.getActorUserAccountId());
        entity.setActionType(aggregate.getActionType());
        entity.setResourceType(aggregate.getResourceType());
        entity.setResourceId(aggregate.getResourceId());
        entity.setSeverity(aggregate.getSeverity());
        entity.setDescription(aggregate.getDescription());
        entity.setIpAddress(aggregate.getIpAddress());
        entity.setOccurredAt(aggregate.getOccurredAt());

        return entity;
    }
}