package com.vitalwatch.center.platform.auditcompliance.interfaces.rest.transform;

import com.vitalwatch.center.platform.auditcompliance.infrastructure.persistence.jpa.entities.AuditLogJpaEntity;
import com.vitalwatch.center.platform.auditcompliance.interfaces.rest.resources.AuditLogResource;

/**
 * Assembler used to convert audit log entities into REST resources.
 */
public final class AuditLogResourceFromEntityAssembler {

    private AuditLogResourceFromEntityAssembler() {
    }

    public static AuditLogResource toResourceFromEntity(AuditLogJpaEntity entity) {
        return new AuditLogResource(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getActorUserId(),
                entity.getType().name(),
                entity.getSeverity().name(),
                entity.getResourceType(),
                entity.getResourceId(),
                entity.getDescription(),
                entity.getCreatedAt()
        );
    }
}