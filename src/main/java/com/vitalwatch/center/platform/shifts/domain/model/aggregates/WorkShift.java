package com.vitalwatch.center.platform.shifts.domain.model.aggregates;

import com.vitalwatch.center.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.vitalwatch.center.platform.shifts.domain.model.commands.CancelWorkShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.CompleteWorkShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.CreateWorkShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.enums.ShiftStatus;
import com.vitalwatch.center.platform.shifts.domain.model.enums.ShiftType;
import com.vitalwatch.center.platform.shifts.domain.model.valueobjects.ShiftLabel;
import com.vitalwatch.center.platform.shifts.domain.model.valueobjects.WorkAreaName;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate root that represents a hospital work shift.
 */
public class WorkShift extends AbstractDomainAggregateRoot<WorkShift> {

    private Long id;
    private Long hospitalWorkspaceId;
    private ShiftLabel label;
    private WorkAreaName workArea;
    private ShiftType shiftType;
    private ShiftStatus status;
    private Instant startsAt;
    private Instant endsAt;
    private Long completedByUserAccountId;
    private Long cancelledByUserAccountId;
    private String cancellationReason;
    private Instant completedAt;
    private Instant cancelledAt;

    public WorkShift(
            Long id,
            Long hospitalWorkspaceId,
            ShiftLabel label,
            WorkAreaName workArea,
            ShiftType shiftType,
            ShiftStatus status,
            Instant startsAt,
            Instant endsAt,
            Long completedByUserAccountId,
            Long cancelledByUserAccountId,
            String cancellationReason,
            Instant completedAt,
            Instant cancelledAt
    ) {
        this.id = id;
        this.hospitalWorkspaceId = validatePositiveId(hospitalWorkspaceId, "hospitalWorkspaceId");
        this.label = Objects.requireNonNull(label, "label must not be null");
        this.workArea = Objects.requireNonNull(workArea, "workArea must not be null");
        this.shiftType = Objects.requireNonNull(shiftType, "shiftType must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.startsAt = Objects.requireNonNull(startsAt, "startsAt must not be null");
        this.endsAt = Objects.requireNonNull(endsAt, "endsAt must not be null");
        this.completedByUserAccountId = completedByUserAccountId;
        this.cancelledByUserAccountId = cancelledByUserAccountId;
        this.cancellationReason = cancellationReason;
        this.completedAt = completedAt;
        this.cancelledAt = cancelledAt;

        validateTimeRange(this.startsAt, this.endsAt);
    }

    public WorkShift(CreateWorkShiftCommand command) {
        this(
                null,
                command.hospitalWorkspaceId(),
                new ShiftLabel(command.label()),
                new WorkAreaName(command.workArea()),
                command.shiftType(),
                ShiftStatus.PLANNED,
                command.startsAt(),
                command.endsAt(),
                null,
                null,
                null,
                null,
                null
        );
    }

    public void complete(CompleteWorkShiftCommand command) {
        if (this.status == ShiftStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled shifts cannot be completed");
        }

        if (this.status == ShiftStatus.COMPLETED) {
            throw new IllegalStateException("Shift is already completed");
        }

        this.status = ShiftStatus.COMPLETED;
        this.completedByUserAccountId = validatePositiveId(command.completedByUserAccountId(), "completedByUserAccountId");
        this.completedAt = Instant.now();
    }

    public void cancel(CancelWorkShiftCommand command) {
        if (this.status == ShiftStatus.COMPLETED) {
            throw new IllegalStateException("Completed shifts cannot be cancelled");
        }

        if (this.status == ShiftStatus.CANCELLED) {
            throw new IllegalStateException("Shift is already cancelled");
        }

        if (command.cancellationReason() == null || command.cancellationReason().isBlank()) {
            throw new IllegalArgumentException("Cancellation reason must not be null or blank");
        }

        this.status = ShiftStatus.CANCELLED;
        this.cancelledByUserAccountId = validatePositiveId(command.cancelledByUserAccountId(), "cancelledByUserAccountId");
        this.cancellationReason = command.cancellationReason().trim();
        this.cancelledAt = Instant.now();
    }

    public boolean isPlanned() {
        return this.status == ShiftStatus.PLANNED;
    }

    public boolean isCancelled() {
        return this.status == ShiftStatus.CANCELLED;
    }

    public boolean isCompleted() {
        return this.status == ShiftStatus.COMPLETED;
    }

    private void validateTimeRange(Instant startsAt, Instant endsAt) {
        if (!endsAt.isAfter(startsAt)) {
            throw new IllegalArgumentException("Shift end date must be after start date");
        }
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

    public ShiftLabel getLabelValue() {
        return label;
    }

    public String getLabel() {
        return label.value();
    }

    public WorkAreaName getWorkAreaValue() {
        return workArea;
    }

    public String getWorkArea() {
        return workArea.value();
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public ShiftStatus getStatus() {
        return status;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public Instant getEndsAt() {
        return endsAt;
    }

    public Long getCompletedByUserAccountId() {
        return completedByUserAccountId;
    }

    public Long getCancelledByUserAccountId() {
        return cancelledByUserAccountId;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }
}