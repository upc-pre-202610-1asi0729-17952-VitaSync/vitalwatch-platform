package com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates;

import com.vitalwatch.center.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CancelRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CompleteRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CreateRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.StartRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryPlanReason;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryPlanStatus;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryPriority;
import com.vitalwatch.center.platform.staffrecovery.domain.model.valueobjects.RecoveryNotes;
import com.vitalwatch.center.platform.staffrecovery.domain.model.valueobjects.RestHours;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate root that represents a staff recovery plan after fatigue risk or incident detection.
 */
public class RecoveryPlan extends AbstractDomainAggregateRoot<RecoveryPlan> {

    private Long id;
    private Long hospitalWorkspaceId;
    private Long userAccountId;
    private Long clinicalRiskAssessmentId;
    private Long incidentId;
    private RecoveryPlanReason reason;
    private RecoveryPriority priority;
    private RecoveryPlanStatus status;
    private RestHours recommendedRestHours;
    private RecoveryNotes notes;
    private String completionNotes;
    private String cancellationReason;
    private Long startedByUserAccountId;
    private Long completedByUserAccountId;
    private Long cancelledByUserAccountId;
    private Instant createdAt;
    private Instant startedAt;
    private Instant completedAt;
    private Instant cancelledAt;

    public RecoveryPlan(
            Long id,
            Long hospitalWorkspaceId,
            Long userAccountId,
            Long clinicalRiskAssessmentId,
            Long incidentId,
            RecoveryPlanReason reason,
            RecoveryPriority priority,
            RecoveryPlanStatus status,
            RestHours recommendedRestHours,
            RecoveryNotes notes,
            String completionNotes,
            String cancellationReason,
            Long startedByUserAccountId,
            Long completedByUserAccountId,
            Long cancelledByUserAccountId,
            Instant createdAt,
            Instant startedAt,
            Instant completedAt,
            Instant cancelledAt
    ) {
        this.id = id;
        this.hospitalWorkspaceId = validatePositiveId(hospitalWorkspaceId, "hospitalWorkspaceId");
        this.userAccountId = validatePositiveId(userAccountId, "userAccountId");
        this.clinicalRiskAssessmentId = clinicalRiskAssessmentId;
        this.incidentId = incidentId;
        this.reason = Objects.requireNonNull(reason, "reason must not be null");
        this.priority = Objects.requireNonNull(priority, "priority must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.recommendedRestHours = Objects.requireNonNull(recommendedRestHours, "recommendedRestHours must not be null");
        this.notes = Objects.requireNonNull(notes, "notes must not be null");
        this.completionNotes = completionNotes;
        this.cancellationReason = cancellationReason;
        this.startedByUserAccountId = startedByUserAccountId;
        this.completedByUserAccountId = completedByUserAccountId;
        this.cancelledByUserAccountId = cancelledByUserAccountId;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.cancelledAt = cancelledAt;
    }

    public RecoveryPlan(CreateRecoveryPlanCommand command) {
        this(
                null,
                command.hospitalWorkspaceId(),
                command.userAccountId(),
                command.clinicalRiskAssessmentId(),
                command.incidentId(),
                command.reason(),
                command.priority(),
                RecoveryPlanStatus.CREATED,
                new RestHours(command.recommendedRestHours()),
                new RecoveryNotes(command.notes()),
                null,
                null,
                null,
                null,
                null,
                Instant.now(),
                null,
                null,
                null
        );
    }

    public void start(StartRecoveryPlanCommand command) {
        if (this.status == RecoveryPlanStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled recovery plans cannot be started");
        }

        if (this.status == RecoveryPlanStatus.COMPLETED) {
            throw new IllegalStateException("Completed recovery plans cannot be started");
        }

        if (this.status == RecoveryPlanStatus.IN_PROGRESS) {
            throw new IllegalStateException("Recovery plan is already in progress");
        }

        this.status = RecoveryPlanStatus.IN_PROGRESS;
        this.startedByUserAccountId = validatePositiveId(command.startedByUserAccountId(), "startedByUserAccountId");
        this.startedAt = Instant.now();
    }

    public void complete(CompleteRecoveryPlanCommand command) {
        if (this.status == RecoveryPlanStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled recovery plans cannot be completed");
        }

        if (this.status == RecoveryPlanStatus.COMPLETED) {
            throw new IllegalStateException("Recovery plan is already completed");
        }

        if (command.completionNotes() == null || command.completionNotes().isBlank()) {
            throw new IllegalArgumentException("Completion notes must not be null or blank");
        }

        this.status = RecoveryPlanStatus.COMPLETED;
        this.completedByUserAccountId = validatePositiveId(command.completedByUserAccountId(), "completedByUserAccountId");
        this.completionNotes = command.completionNotes().trim();
        this.completedAt = Instant.now();
    }

    public void cancel(CancelRecoveryPlanCommand command) {
        if (this.status == RecoveryPlanStatus.COMPLETED) {
            throw new IllegalStateException("Completed recovery plans cannot be cancelled");
        }

        if (this.status == RecoveryPlanStatus.CANCELLED) {
            throw new IllegalStateException("Recovery plan is already cancelled");
        }

        if (command.cancellationReason() == null || command.cancellationReason().isBlank()) {
            throw new IllegalArgumentException("Cancellation reason must not be null or blank");
        }

        this.status = RecoveryPlanStatus.CANCELLED;
        this.cancelledByUserAccountId = validatePositiveId(command.cancelledByUserAccountId(), "cancelledByUserAccountId");
        this.cancellationReason = command.cancellationReason().trim();
        this.cancelledAt = Instant.now();
    }

    public boolean isCreated() {
        return this.status == RecoveryPlanStatus.CREATED;
    }

    public boolean isInProgress() {
        return this.status == RecoveryPlanStatus.IN_PROGRESS;
    }

    public boolean isCompleted() {
        return this.status == RecoveryPlanStatus.COMPLETED;
    }

    public boolean isCancelled() {
        return this.status == RecoveryPlanStatus.CANCELLED;
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

    public Long getUserAccountId() {
        return userAccountId;
    }

    public Long getClinicalRiskAssessmentId() {
        return clinicalRiskAssessmentId;
    }

    public Long getIncidentId() {
        return incidentId;
    }

    public RecoveryPlanReason getReason() {
        return reason;
    }

    public RecoveryPriority getPriority() {
        return priority;
    }

    public RecoveryPlanStatus getStatus() {
        return status;
    }

    public RestHours getRecommendedRestHoursValue() {
        return recommendedRestHours;
    }

    public Double getRecommendedRestHours() {
        return recommendedRestHours.value();
    }

    public RecoveryNotes getNotesValue() {
        return notes;
    }

    public String getNotes() {
        return notes.value();
    }

    public String getCompletionNotes() {
        return completionNotes;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public Long getStartedByUserAccountId() {
        return startedByUserAccountId;
    }

    public Long getCompletedByUserAccountId() {
        return completedByUserAccountId;
    }

    public Long getCancelledByUserAccountId() {
        return cancelledByUserAccountId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }
}