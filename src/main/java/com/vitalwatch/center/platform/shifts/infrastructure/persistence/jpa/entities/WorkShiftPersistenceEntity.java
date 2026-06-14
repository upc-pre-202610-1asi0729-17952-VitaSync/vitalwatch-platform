package com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.vitalwatch.center.platform.shifts.domain.model.enums.ShiftStatus;
import com.vitalwatch.center.platform.shifts.domain.model.enums.ShiftType;
import jakarta.persistence.*;

import java.time.Instant;

/**
 * JPA persistence entity for work shifts.
 */
@Entity
@Table(name = "work_shifts")
public class WorkShiftPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "hospital_workspace_id", nullable = false)
    private Long hospitalWorkspaceId;

    @Column(nullable = false, length = 120)
    private String label;

    @Column(name = "work_area", nullable = false, length = 120)
    private String workArea;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift_type", nullable = false)
    private ShiftType shiftType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShiftStatus status;

    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;

    @Column(name = "ends_at", nullable = false)
    private Instant endsAt;

    @Column(name = "completed_by_user_account_id")
    private Long completedByUserAccountId;

    @Column(name = "cancelled_by_user_account_id")
    private Long cancelledByUserAccountId;

    @Column(name = "cancellation_reason", length = 1000)
    private String cancellationReason;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    public WorkShiftPersistenceEntity() {
    }

    public Long getHospitalWorkspaceId() {
        return hospitalWorkspaceId;
    }

    public void setHospitalWorkspaceId(Long hospitalWorkspaceId) {
        this.hospitalWorkspaceId = hospitalWorkspaceId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getWorkArea() {
        return workArea;
    }

    public void setWorkArea(String workArea) {
        this.workArea = workArea;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public ShiftStatus getStatus() {
        return status;
    }

    public void setStatus(ShiftStatus status) {
        this.status = status;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public Instant getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(Instant endsAt) {
        this.endsAt = endsAt;
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

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
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