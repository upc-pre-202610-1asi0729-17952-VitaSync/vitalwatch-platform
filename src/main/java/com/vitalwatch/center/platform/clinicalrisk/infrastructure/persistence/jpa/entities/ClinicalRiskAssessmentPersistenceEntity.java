package com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.enums.AssessmentStatus;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.enums.RiskLevel;
import com.vitalwatch.center.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;

import java.time.Instant;

/**
 * JPA persistence entity for clinical risk assessments.
 */
@Entity
@Table(name = "clinical_risk_assessments")
public class ClinicalRiskAssessmentPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "hospital_workspace_id", nullable = false)
    private Long hospitalWorkspaceId;

    @Column(name = "user_account_id", nullable = false)
    private Long userAccountId;

    @Column(name = "vital_sign_reading_id", nullable = false)
    private Long vitalSignReadingId;

    @Column(name = "fatigue_score", nullable = false)
    private Integer fatigueScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssessmentStatus status;

    @Column(name = "assessed_at", nullable = false)
    private Instant assessedAt;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    @Column(name = "escalated_at")
    private Instant escalatedAt;

    @Column(name = "closed_at")
    private Instant closedAt;

    public ClinicalRiskAssessmentPersistenceEntity() {
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

    public Long getVitalSignReadingId() {
        return vitalSignReadingId;
    }

    public void setVitalSignReadingId(Long vitalSignReadingId) {
        this.vitalSignReadingId = vitalSignReadingId;
    }

    public Integer getFatigueScore() {
        return fatigueScore;
    }

    public void setFatigueScore(Integer fatigueScore) {
        this.fatigueScore = fatigueScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public AssessmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssessmentStatus status) {
        this.status = status;
    }

    public Instant getAssessedAt() {
        return assessedAt;
    }

    public void setAssessedAt(Instant assessedAt) {
        this.assessedAt = assessedAt;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Instant reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public Instant getEscalatedAt() {
        return escalatedAt;
    }

    public void setEscalatedAt(Instant escalatedAt) {
        this.escalatedAt = escalatedAt;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }
}