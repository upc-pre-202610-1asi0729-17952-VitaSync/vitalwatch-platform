package com.vitalwatch.center.platform.audit.interfaces.rest.transform;

import com.vitalwatch.center.platform.audit.domain.model.aggregates.AuditLog;
import com.vitalwatch.center.platform.audit.interfaces.rest.resources.AuditLogResource;

/**
 * Assembler to convert AuditLog aggregate into AuditLogResource.
 */
public final class AuditLogResourceFromEntityAssembler {

    private AuditLogResourceFromEntityAssembler() {
    }

    public static AuditLogResource toResourceFromEntity(AuditLog entity) {
        return new AuditLogResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getActorUserAccountId(),
                entity.getActionType(),
                entity.getResourceType(),
                entity.getResourceId(),
                entity.getSeverity(),
                entity.getDescription(),
                entity.getIpAddress(),
                entity.getOccurredAt()
        );
    }
}