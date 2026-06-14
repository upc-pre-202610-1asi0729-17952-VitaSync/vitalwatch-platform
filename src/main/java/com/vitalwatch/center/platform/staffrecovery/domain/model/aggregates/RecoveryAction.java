package com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates;

import com.vitalwatch.center.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.AddRecoveryActionCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CompleteRecoveryActionCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryActionType;
import com.vitalwatch.center.platform.staffrecovery.domain.model.valueobjects.RecoveryNotes;
import com.vitalwatch.center.platform.staffrecovery.domain.model.valueobjects.RestHours;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate root that represents one recovery action inside a staff recovery plan.
 */
public class RecoveryAction extends AbstractDomainAggregateRoot<RecoveryAction> {

    private Long id;
    private Long recoveryPlanId;
    private RecoveryActionType actionType;
    private RecoveryNotes notes;
    private RestHours recommendedRestHours;
    private Boolean completed;
    private Long completedByUserAccountId;
    private Instant createdAt;
    private Instant completedAt;

    public RecoveryAction(
            Long id,
            Long recoveryPlanId,
            RecoveryActionType actionType,
            RecoveryNotes notes,
            RestHours recommendedRestHours,
            Boolean completed,
            Long completedByUserAccountId,
            Instant createdAt,
            Instant completedAt
    ) {
        this.id = id;
        this.recoveryPlanId = validatePositiveId(recoveryPlanId, "recoveryPlanId");
        this.actionType = Objects.requireNonNull(actionType, "actionType must not be null");
        this.notes = Objects.requireNonNull(notes, "notes must not be null");
        this.recommendedRestHours = Objects.requireNonNull(recommendedRestHours, "recommendedRestHours must not be null");
        this.completed = Objects.requireNonNull(completed, "completed must not be null");
        this.completedByUserAccountId = completedByUserAccountId;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.completedAt = completedAt;
    }

    public RecoveryAction(AddRecoveryActionCommand command) {
        this(
                null,
                command.recoveryPlanId(),
                command.actionType(),
                new RecoveryNotes(command.notes()),
                new RestHours(command.recommendedRestHours()),
                false,
                null,
                Instant.now(),
                null
        );
    }

    public void complete(CompleteRecoveryActionCommand command) {
        if (Boolean.TRUE.equals(this.completed)) {
            throw new IllegalStateException("Recovery action is already completed");
        }

        this.completed = true;
        this.completedByUserAccountId = validatePositiveId(command.completedByUserAccountId(), "completedByUserAccountId");
        this.completedAt = Instant.now();
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

    public Long getRecoveryPlanId() {
        return recoveryPlanId;
    }

    public RecoveryActionType getActionType() {
        return actionType;
    }

    public RecoveryNotes getNotesValue() {
        return notes;
    }

    public String getNotes() {
        return notes.value();
    }

    public RestHours getRecommendedRestHoursValue() {
        return recommendedRestHours;
    }

    public Double getRecommendedRestHours() {
        return recommendedRestHours.value();
    }

    public Boolean getCompleted() {
        return completed;
    }

    public Long getCompletedByUserAccountId() {
        return completedByUserAccountId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }
}