package com.vitalwatch.center.platform.incidents.interfaces.rest.transform;

import com.vitalwatch.center.platform.incidents.domain.model.aggregates.Incident;
import com.vitalwatch.center.platform.incidents.interfaces.rest.resources.IncidentResource;

/**
 * Assembler to convert Incident aggregate into IncidentResource.
 */
public final class IncidentResourceFromEntityAssembler {

    private IncidentResourceFromEntityAssembler() {
    }

    public static IncidentResource toResourceFromEntity(Incident entity) {
        return new IncidentResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getReportedUserAccountId(),
                entity.getClinicalRiskAssessmentId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getSeverity(),
                entity.getSource(),
                entity.getStatus(),
                entity.getEscalationLevel(),
                entity.getAcknowledgedByUserAccountId(),
                entity.getEscalatedByUserAccountId(),
                entity.getResolvedByUserAccountId(),
                entity.getCancelledByUserAccountId(),
                entity.getResolutionNotes(),
                entity.getCancellationReason(),
                entity.getCreatedAt(),
                entity.getAcknowledgedAt(),
                entity.getEscalatedAt(),
                entity.getResolvedAt(),
                entity.getCancelledAt()
        );
    }
}