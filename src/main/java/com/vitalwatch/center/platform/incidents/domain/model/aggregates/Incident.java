package com.vitalwatch.center.platform.incidents.domain.model.aggregates;

import com.vitalwatch.center.platform.incidents.domain.model.commands.CreateIncidentCommand;
import com.vitalwatch.center.platform.incidents.domain.model.enums.EscalationLevel;
import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentSeverity;
import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentSource;
import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentStatus;
import com.vitalwatch.center.platform.incidents.domain.model.valueobjects.IncidentDescription;
import com.vitalwatch.center.platform.incidents.domain.model.valueobjects.IncidentTitle;
import com.vitalwatch.center.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate root that represents an incident requiring attention inside a hospital workspace.
 */
public class Incident extends AbstractDomainAggregateRoot<Incident> {

    private Long id;
    private Long hospitalWorkspaceId;
    private Long reportedUserAccountId;
    private Long clinicalRiskAssessmentId;
    private IncidentTitle title;
    private IncidentDescription description;
    private IncidentSeverity severity;
    private IncidentSource source;
    private IncidentStatus status;
    private EscalationLevel escalationLevel;
    private Long acknowledgedByUserAccountId;
    private Long escalatedByUserAccountId;
    private Long resolvedByUserAccountId;
    private Long cancelledByUserAccountId;
    private String resolutionNotes;
    private String cancellationReason;
    private Instant createdAt;
    private Instant acknowledgedAt;
    private Instant escalatedAt;
    private Instant resolvedAt;
    private Instant cancelledAt;

    public Incident(
            Long id,
            Long hospitalWorkspaceId,
            Long reportedUserAccountId,
            Long clinicalRiskAssessmentId,
            IncidentTitle title,
            IncidentDescription description,
            IncidentSeverity severity,
            IncidentSource source,
            IncidentStatus status,
            EscalationLevel escalationLevel,
            Long acknowledgedByUserAccountId,
            Long escalatedByUserAccountId,
            Long resolvedByUserAccountId,
            Long cancelledByUserAccountId,
            String resolutionNotes,
            String cancellationReason,
            Instant createdAt,
            Instant acknowledgedAt,
            Instant escalatedAt,
            Instant resolvedAt,
            Instant cancelledAt
    ) {
        this.id = id;
        this.hospitalWorkspaceId = validatePositiveId(hospitalWorkspaceId, "hospitalWorkspaceId");
        this.reportedUserAccountId = validatePositiveId(reportedUserAccountId, "reportedUserAccountId");
        this.clinicalRiskAssessmentId = clinicalRiskAssessmentId;
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
        this.severity = Objects.requireNonNull(severity, "severity must not be null");
        this.source = Objects.requireNonNull(source, "source must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.escalationLevel = Objects.requireNonNull(escalationLevel, "escalationLevel must not be null");
        this.acknowledgedByUserAccountId = acknowledgedByUserAccountId;
        this.escalatedByUserAccountId = escalatedByUserAccountId;
        this.resolvedByUserAccountId = resolvedByUserAccountId;
        this.cancelledByUserAccountId = cancelledByUserAccountId;
        this.resolutionNotes = resolutionNotes;
        this.cancellationReason = cancellationReason;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.acknowledgedAt = acknowledgedAt;
        this.escalatedAt = escalatedAt;
        this.resolvedAt = resolvedAt;
        this.cancelledAt = cancelledAt;
    }

    public Incident(CreateIncidentCommand command) {
        this(
                null,
                command.hospitalWorkspaceId(),
                command.reportedUserAccountId(),
                command.clinicalRiskAssessmentId(),
                new IncidentTitle(command.title()),
                new IncidentDescription(command.description()),
                command.severity(),
                command.source(),
                IncidentStatus.OPEN,
                EscalationLevel.NONE,
                null,
                null,
                null,
                null,
                null,
                null,
                Instant.now(),
                null,
                null,
                null,
                null
        );
    }

    public void acknowledge(Long acknowledgedByUserAccountId) {
        if (this.status == IncidentStatus.RESOLVED || this.status == IncidentStatus.CANCELLED) {
            throw new IllegalStateException("Resolved or cancelled incidents cannot be acknowledged");
        }

        this.status = IncidentStatus.ACKNOWLEDGED;
        this.acknowledgedByUserAccountId = validatePositiveId(acknowledgedByUserAccountId, "acknowledgedByUserAccountId");
        this.acknowledgedAt = Instant.now();
    }

    public void escalate(Long escalatedByUserAccountId, EscalationLevel escalationLevel) {
        if (this.status == IncidentStatus.RESOLVED || this.status == IncidentStatus.CANCELLED) {
            throw new IllegalStateException("Resolved or cancelled incidents cannot be escalated");
        }

        if (escalationLevel == null || escalationLevel == EscalationLevel.NONE) {
            throw new IllegalArgumentException("Escalation level must be higher than NONE");
        }

        if (this.severity == IncidentSeverity.LOW || this.severity == IncidentSeverity.MODERATE) {
            throw new IllegalStateException("Only high or critical incidents can be escalated");
        }

        this.status = IncidentStatus.ESCALATED;
        this.escalationLevel = escalationLevel;
        this.escalatedByUserAccountId = validatePositiveId(escalatedByUserAccountId, "escalatedByUserAccountId");
        this.escalatedAt = Instant.now();
    }

    public void resolve(Long resolvedByUserAccountId, String resolutionNotes) {
        if (this.status == IncidentStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled incidents cannot be resolved");
        }

        if (this.status == IncidentStatus.RESOLVED) {
            throw new IllegalStateException("Incident is already resolved");
        }

        if (resolutionNotes == null || resolutionNotes.isBlank()) {
            throw new IllegalArgumentException("Resolution notes must not be null or blank");
        }

        this.status = IncidentStatus.RESOLVED;
        this.resolvedByUserAccountId = validatePositiveId(resolvedByUserAccountId, "resolvedByUserAccountId");
        this.resolutionNotes = resolutionNotes.trim();
        this.resolvedAt = Instant.now();
    }

    public void cancel(Long cancelledByUserAccountId, String cancellationReason) {
        if (this.status == IncidentStatus.RESOLVED) {
            throw new IllegalStateException("Resolved incidents cannot be cancelled");
        }

        if (this.status == IncidentStatus.CANCELLED) {
            throw new IllegalStateException("Incident is already cancelled");
        }

        if (cancellationReason == null || cancellationReason.isBlank()) {
            throw new IllegalArgumentException("Cancellation reason must not be null or blank");
        }

        this.status = IncidentStatus.CANCELLED;
        this.cancelledByUserAccountId = validatePositiveId(cancelledByUserAccountId, "cancelledByUserAccountId");
        this.cancellationReason = cancellationReason.trim();
        this.cancelledAt = Instant.now();
    }

    public boolean isOpen() {
        return this.status == IncidentStatus.OPEN;
    }

    public boolean isEscalated() {
        return this.status == IncidentStatus.ESCALATED;
    }

    private Long validatePositiveId(Long value, String fieldName) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be a positive number");
        }
        return value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHospitalWorkspaceId() {
        return hospitalWorkspaceId;
    }

    public Long getReportedUserAccountId() {
        return reportedUserAccountId;
    }

    public Long getClinicalRiskAssessmentId() {
        return clinicalRiskAssessmentId;
    }

    public IncidentTitle getTitleValue() {
        return title;
    }

    public String getTitle() {
        return title.value();
    }

    public IncidentDescription getDescriptionValue() {
        return description;
    }

    public String getDescription() {
        return description.value();
    }

    public IncidentSeverity getSeverity() {
        return severity;
    }

    public IncidentSource getSource() {
        return source;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public EscalationLevel getEscalationLevel() {
        return escalationLevel;
    }

    public Long getAcknowledgedByUserAccountId() {
        return acknowledgedByUserAccountId;
    }

    public Long getEscalatedByUserAccountId() {
        return escalatedByUserAccountId;
    }

    public Long getResolvedByUserAccountId() {
        return resolvedByUserAccountId;
    }

    public Long getCancelledByUserAccountId() {
        return cancelledByUserAccountId;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public Instant getEscalatedAt() {
        return escalatedAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }
}