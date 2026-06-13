package com.vitalwatch.center.platform.incidents.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.incidents.domain.model.enums.EscalationLevel;
import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentSeverity;
import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentSource;
import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentStatus;
import com.vitalwatch.center.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;

import java.time.Instant;

/**
 * JPA persistence entity for incidents.
 */
@Entity
@Table(name = "incidents")
public class IncidentPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "hospital_workspace_id", nullable = false)
    private Long hospitalWorkspaceId;

    @Column(name = "reported_user_account_id", nullable = false)
    private Long reportedUserAccountId;

    @Column(name = "clinical_risk_assessment_id")
    private Long clinicalRiskAssessmentId;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentSource source;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "escalation_level", nullable = false)
    private EscalationLevel escalationLevel;

    @Column(name = "acknowledged_by_user_account_id")
    private Long acknowledgedByUserAccountId;

    @Column(name = "escalated_by_user_account_id")
    private Long escalatedByUserAccountId;

    @Column(name = "resolved_by_user_account_id")
    private Long resolvedByUserAccountId;

    @Column(name = "cancelled_by_user_account_id")
    private Long cancelledByUserAccountId;

    @Column(name = "resolution_notes", length = 1000)
    private String resolutionNotes;

    @Column(name = "cancellation_reason", length = 1000)
    private String cancellationReason;

    @Column(name = "incident_created_at", nullable = false)
    private Instant incidentCreatedAt;

    @Column(name = "acknowledged_at")
    private Instant acknowledgedAt;

    @Column(name = "incident_escalated_at")
    private Instant escalatedAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    public IncidentPersistenceEntity() {
    }

    public Long getHospitalWorkspaceId() {
        return hospitalWorkspaceId;
    }

    public void setHospitalWorkspaceId(Long hospitalWorkspaceId) {
        this.hospitalWorkspaceId = hospitalWorkspaceId;
    }

    public Long getReportedUserAccountId() {
        return reportedUserAccountId;
    }

    public void setReportedUserAccountId(Long reportedUserAccountId) {
        this.reportedUserAccountId = reportedUserAccountId;
    }

    public Long getClinicalRiskAssessmentId() {
        return clinicalRiskAssessmentId;
    }

    public void setClinicalRiskAssessmentId(Long clinicalRiskAssessmentId) {
        this.clinicalRiskAssessmentId = clinicalRiskAssessmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IncidentSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(IncidentSeverity severity) {
        this.severity = severity;
    }

    public IncidentSource getSource() {
        return source;
    }

    public void setSource(IncidentSource source) {
        this.source = source;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public EscalationLevel getEscalationLevel() {
        return escalationLevel;
    }

    public void setEscalationLevel(EscalationLevel escalationLevel) {
        this.escalationLevel = escalationLevel;
    }

    public Long getAcknowledgedByUserAccountId() {
        return acknowledgedByUserAccountId;
    }

    public void setAcknowledgedByUserAccountId(Long acknowledgedByUserAccountId) {
        this.acknowledgedByUserAccountId = acknowledgedByUserAccountId;
    }

    public Long getEscalatedByUserAccountId() {
        return escalatedByUserAccountId;
    }

    public void setEscalatedByUserAccountId(Long escalatedByUserAccountId) {
        this.escalatedByUserAccountId = escalatedByUserAccountId;
    }

    public Long getResolvedByUserAccountId() {
        return resolvedByUserAccountId;
    }

    public void setResolvedByUserAccountId(Long resolvedByUserAccountId) {
        this.resolvedByUserAccountId = resolvedByUserAccountId;
    }

    public Long getCancelledByUserAccountId() {
        return cancelledByUserAccountId;
    }

    public void setCancelledByUserAccountId(Long cancelledByUserAccountId) {
        this.cancelledByUserAccountId = cancelledByUserAccountId;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Instant getIncidentCreatedAt() {
        return incidentCreatedAt;
    }

    public void setIncidentCreatedAt(Instant incidentCreatedAt) {
        this.incidentCreatedAt = incidentCreatedAt;
    }

    public Instant getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public void setAcknowledgedAt(Instant acknowledgedAt) {
        this.acknowledgedAt = acknowledgedAt;
    }

    public Instant getEscalatedAt() {
        return escalatedAt;
    }

    public void setEscalatedAt(Instant escalatedAt) {
        this.escalatedAt = escalatedAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Instant cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
}
