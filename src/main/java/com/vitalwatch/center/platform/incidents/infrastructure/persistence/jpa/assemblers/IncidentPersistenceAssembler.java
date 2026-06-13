package com.vitalwatch.center.platform.incidents.infrastructure.persistence.jpa.assemblers;

import com.vitalwatch.center.platform.incidents.domain.model.aggregates.Incident;
import com.vitalwatch.center.platform.incidents.domain.model.valueobjects.IncidentDescription;
import com.vitalwatch.center.platform.incidents.domain.model.valueobjects.IncidentTitle;
import com.vitalwatch.center.platform.incidents.infrastructure.persistence.jpa.entities.IncidentPersistenceEntity;

/**
 * Assembler between Incident domain and persistence representations.
 */
public final class IncidentPersistenceAssembler {

    private IncidentPersistenceAssembler() {
    }

    public static Incident toDomainFromPersistence(IncidentPersistenceEntity entity) {
        return new Incident(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getReportedUserAccountId(),
                entity.getClinicalRiskAssessmentId(),
                new IncidentTitle(entity.getTitle()),
                new IncidentDescription(entity.getDescription()),
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
                entity.getIncidentCreatedAt(),
                entity.getAcknowledgedAt(),
                entity.getEscalatedAt(),
                entity.getResolvedAt(),
                entity.getCancelledAt()
        );
    }

    public static IncidentPersistenceEntity toPersistenceFromDomain(Incident aggregate) {
        var entity = new IncidentPersistenceEntity();

        entity.setId(aggregate.getId());
        entity.setHospitalWorkspaceId(aggregate.getHospitalWorkspaceId());
        entity.setReportedUserAccountId(aggregate.getReportedUserAccountId());
        entity.setClinicalRiskAssessmentId(aggregate.getClinicalRiskAssessmentId());
        entity.setTitle(aggregate.getTitle());
        entity.setDescription(aggregate.getDescription());
        entity.setSeverity(aggregate.getSeverity());
        entity.setSource(aggregate.getSource());
        entity.setStatus(aggregate.getStatus());
        entity.setEscalationLevel(aggregate.getEscalationLevel());
        entity.setAcknowledgedByUserAccountId(aggregate.getAcknowledgedByUserAccountId());
        entity.setEscalatedByUserAccountId(aggregate.getEscalatedByUserAccountId());
        entity.setResolvedByUserAccountId(aggregate.getResolvedByUserAccountId());
        entity.setCancelledByUserAccountId(aggregate.getCancelledByUserAccountId());
        entity.setResolutionNotes(aggregate.getResolutionNotes());
        entity.setCancellationReason(aggregate.getCancellationReason());
        entity.setIncidentCreatedAt(aggregate.getCreatedAt());
        entity.setAcknowledgedAt(aggregate.getAcknowledgedAt());
        entity.setEscalatedAt(aggregate.getEscalatedAt());
        entity.setResolvedAt(aggregate.getResolvedAt());
        entity.setCancelledAt(aggregate.getCancelledAt());

        return entity;
    }
}
