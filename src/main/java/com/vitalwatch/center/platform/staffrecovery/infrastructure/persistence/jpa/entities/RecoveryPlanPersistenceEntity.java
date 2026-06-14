package com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryPlanReason;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryPlanStatus;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryPriority;
import jakarta.persistence.*;

import java.time.Instant;

/**
 * JPA persistence entity for staff recovery plans.
 */
@Entity
@Table(name = "recovery_plans")
public class RecoveryPlanPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "hospital_workspace_id", nullable = false)
    private Long hospitalWorkspaceId;

    @Column(name = "user_account_id", nullable = false)
    private Long userAccountId;

    @Column(name = "clinical_risk_assessment_id")
    private Long clinicalRiskAssessmentId;

    @Column(name = "incident_id")
    private Long incidentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecoveryPlanReason reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecoveryPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecoveryPlanStatus status;

    @Column(name = "recommended_rest_hours", nullable = false)
    private Double recommendedRestHours;

    @Column(nullable = false, length = 1000)
    private String notes;

    @Column(name = "completion_notes", length = 1000)
    private String completionNotes;

    @Column(name = "cancellation_reason", length = 1000)
    private String cancellationReason;

    @Column(name = "started_by_user_account_id")
    private Long startedByUserAccountId;

    @Column(name = "completed_by_user_account_id")
    private Long completedByUserAccountId;

    @Column(name = "cancelled_by_user_account_id")
    private Long cancelledByUserAccountId;

    @Column(name = "plan_created_at", nullable = false)
    private Instant planCreatedAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    public RecoveryPlanPersistenceEntity() {
    }

    public Long getHospitalWorkspaceId() {
        return hospitalWorkspaceId;
    }

    public void setHospitalWorkspaceId(Long hospitalWorkspaceId) {
        this.hospitalWorkspaceId = hospitalWorkspaceId;
    }

    public Long getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Long userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Long getClinicalRiskAssessmentId() {
        return clinicalRiskAssessmentId;
    }

    public void setClinicalRiskAssessmentId(Long clinicalRiskAssessmentId) {
        this.clinicalRiskAssessmentId = clinicalRiskAssessmentId;
    }

    public Long getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Long incidentId) {
        this.incidentId = incidentId;
    }

    public RecoveryPlanReason getReason() {
        return reason;
    }

    public void setReason(RecoveryPlanReason reason) {
        this.reason = reason;
    }

    public RecoveryPriority getPriority() {
        return priority;
    }

    public void setPriority(RecoveryPriority priority) {
        this.priority = priority;
    }

    public RecoveryPlanStatus getStatus() {
        return status;
    }

    public void setStatus(RecoveryPlanStatus status) {
        this.status = status;
    }

    public Double getRecommendedRestHours() {
        return recommendedRestHours;
    }

    public void setRecommendedRestHours(Double recommendedRestHours) {
        this.recommendedRestHours = recommendedRestHours;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCompletionNotes() {
        return completionNotes;
    }

    public void setCompletionNotes(String completionNotes) {
        this.completionNotes = completionNotes;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Long getStartedByUserAccountId() {
        return startedByUserAccountId;
    }

    public void setStartedByUserAccountId(Long startedByUserAccountId) {
        this.startedByUserAccountId = startedByUserAccountId;
    }

    public Long getCompletedByUserAccountId() {
        return completedByUserAccountId;
    }

    public void setCompletedByUserAccountId(Long completedByUserAccountId) {
        this.completedByUserAccountId = completedByUserAccountId;
    }

    public Long getCancelledByUserAccountId() {
        return cancelledByUserAccountId;
    }

    public void setCancelledByUserAccountId(Long cancelledByUserAccountId) {
        this.cancelledByUserAccountId = cancelledByUserAccountId;
    }

    public Instant getPlanCreatedAt() {
        return planCreatedAt;
    }

    public void setPlanCreatedAt(Instant planCreatedAt) {
        this.planCreatedAt = planCreatedAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Instant cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
}