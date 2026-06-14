package com.vitalwatch.center.platform.shifts.domain.model.aggregates;

import com.vitalwatch.center.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.vitalwatch.center.platform.shifts.domain.model.commands.AssignUserToShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.ConfirmShiftAssignmentCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.ReleaseShiftAssignmentCommand;
import com.vitalwatch.center.platform.shifts.domain.model.enums.ShiftAssignmentStatus;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate root that represents the assignment of a user account to a work shift.
 */
public class ShiftAssignment extends AbstractDomainAggregateRoot<ShiftAssignment> {

    private Long id;
    private Long workShiftId;
    private Long userAccountId;
    private Long assignedByUserAccountId;
    private Long confirmedByUserAccountId;
    private Long releasedByUserAccountId;
    private ShiftAssignmentStatus status;
    private String releaseReason;
    private Instant assignedAt;
    private Instant confirmedAt;
    private Instant releasedAt;

    public ShiftAssignment(
            Long id,
            Long workShiftId,
            Long userAccountId,
            Long assignedByUserAccountId,
            Long confirmedByUserAccountId,
            Long releasedByUserAccountId,
            ShiftAssignmentStatus status,
            String releaseReason,
            Instant assignedAt,
            Instant confirmedAt,
            Instant releasedAt
    ) {
        this.id = id;
        this.workShiftId = validatePositiveId(workShiftId, "workShiftId");
        this.userAccountId = validatePositiveId(userAccountId, "userAccountId");
        this.assignedByUserAccountId = validatePositiveId(assignedByUserAccountId, "assignedByUserAccountId");
        this.confirmedByUserAccountId = confirmedByUserAccountId;
        this.releasedByUserAccountId = releasedByUserAccountId;
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.releaseReason = releaseReason;
        this.assignedAt = Objects.requireNonNull(assignedAt, "assignedAt must not be null");
        this.confirmedAt = confirmedAt;
        this.releasedAt = releasedAt;
    }

    public ShiftAssignment(AssignUserToShiftCommand command) {
        this(
                null,
                command.workShiftId(),
                command.userAccountId(),
                command.assignedByUserAccountId(),
                null,
                null,
                ShiftAssignmentStatus.ASSIGNED,
                null,
                Instant.now(),
                null,
                null
        );
    }

    public void confirm(ConfirmShiftAssignmentCommand command) {
        if (this.status == ShiftAssignmentStatus.RELEASED || this.status == ShiftAssignmentStatus.CANCELLED) {
            throw new IllegalStateException("Released or cancelled assignments cannot be confirmed");
        }

        if (this.status == ShiftAssignmentStatus.CONFIRMED) {
            throw new IllegalStateException("Assignment is already confirmed");
        }

        this.status = ShiftAssignmentStatus.CONFIRMED;
        this.confirmedByUserAccountId = validatePositiveId(command.confirmedByUserAccountId(), "confirmedByUserAccountId");
        this.confirmedAt = Instant.now();
    }

    public void release(ReleaseShiftAssignmentCommand command) {
        if (this.status == ShiftAssignmentStatus.RELEASED) {
            throw new IllegalStateException("Assignment is already released");
        }

        if (this.status == ShiftAssignmentStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled assignments cannot be released");
        }

        if (command.releaseReason() == null || command.releaseReason().isBlank()) {
            throw new IllegalArgumentException("Release reason must not be null or blank");
        }

        this.status = ShiftAssignmentStatus.RELEASED;
        this.releasedByUserAccountId = validatePositiveId(command.releasedByUserAccountId(), "releasedByUserAccountId");
        this.releaseReason = command.releaseReason().trim();
        this.releasedAt = Instant.now();
    }

    public void cancel() {
        if (this.status == ShiftAssignmentStatus.RELEASED) {
            throw new IllegalStateException("Released assignments cannot be cancelled");
        }

        this.status = ShiftAssignmentStatus.CANCELLED;
    }

    public boolean isActive() {
        return this.status == ShiftAssignmentStatus.ASSIGNED || this.status == ShiftAssignmentStatus.CONFIRMED;
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

    public Long getWorkShiftId() {
        return workShiftId;
    }

    public Long getUserAccountId() {
        return userAccountId;
    }

    public Long getAssignedByUserAccountId() {
        return assignedByUserAccountId;
    }

    public Long getConfirmedByUserAccountId() {
        return confirmedByUserAccountId;
    }

    public Long getReleasedByUserAccountId() {
        return releasedByUserAccountId;
    }

    public ShiftAssignmentStatus getStatus() {
        return status;
    }

    public String getReleaseReason() {
        return releaseReason;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public Instant getConfirmedAt() {
        return confirmedAt;
    }

    public Instant getReleasedAt() {
        return releasedAt;
    }
}