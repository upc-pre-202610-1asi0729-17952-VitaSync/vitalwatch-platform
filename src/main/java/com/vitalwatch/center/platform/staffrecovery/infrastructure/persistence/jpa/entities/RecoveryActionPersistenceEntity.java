package com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryActionType;
import jakarta.persistence.*;

import java.time.Instant;

/**
 * JPA persistence entity for staff recovery actions.
 */
@Entity
@Table(name = "recovery_actions")
public class RecoveryActionPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "recovery_plan_id", nullable = false)
    private Long recoveryPlanId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private RecoveryActionType actionType;

    @Column(nullable = false, length = 1000)
    private String notes;

    @Column(name = "recommended_rest_hours", nullable = false)
    private Double recommendedRestHours;

    @Column(nullable = false)
    private Boolean completed;

    @Column(name = "completed_by_user_account_id")
    private Long completedByUserAccountId;

    @Column(name = "action_created_at", nullable = false)
    private Instant actionCreatedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    public RecoveryActionPersistenceEntity() {
    }

    public Long getRecoveryPlanId() {
        return recoveryPlanId;
    }

    public void setRecoveryPlanId(Long recoveryPlanId) {
        this.recoveryPlanId = recoveryPlanId;
    }

    public RecoveryActionType getActionType() {
        return actionType;
    }

    public void setActionType(RecoveryActionType actionType) {
        this.actionType = actionType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getRecommendedRestHours() {
        return recommendedRestHours;
    }

    public void setRecommendedRestHours(Double recommendedRestHours) {
        this.recommendedRestHours = recommendedRestHours;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Long getCompletedByUserAccountId() {
        return completedByUserAccountId;
    }

    public void setCompletedByUserAccountId(Long completedByUserAccountId) {
        this.completedByUserAccountId = completedByUserAccountId;
    }

    public Instant getActionCreatedAt() {
        return actionCreatedAt;
    }

    public void setActionCreatedAt(Instant actionCreatedAt) {
        this.actionCreatedAt = actionCreatedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }
}