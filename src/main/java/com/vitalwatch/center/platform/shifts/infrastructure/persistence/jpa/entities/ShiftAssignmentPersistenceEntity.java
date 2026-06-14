package com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.vitalwatch.center.platform.shifts.domain.model.enums.ShiftAssignmentStatus;
import jakarta.persistence.*;

import java.time.Instant;

/**
 * JPA persistence entity for shift assignments.
 */
@Entity
@Table(name = "shift_assignments")
public class ShiftAssignmentPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "work_shift_id", nullable = false)
    private Long workShiftId;

    @Column(name = "user_account_id", nullable = false)
    private Long userAccountId;

    @Column(name = "assigned_by_user_account_id", nullable = false)
    private Long assignedByUserAccountId;

    @Column(name = "confirmed_by_user_account_id")
    private Long confirmedByUserAccountId;

    @Column(name = "released_by_user_account_id")
    private Long releasedByUserAccountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShiftAssignmentStatus status;

    @Column(name = "release_reason", length = 1000)
    private String releaseReason;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;

    @Column(name = "confirmed_at")
    private Instant confirmedAt;

    @Column(name = "released_at")
    private Instant releasedAt;

    public ShiftAssignmentPersistenceEntity() {
    }

    public Long getWorkShiftId() {
        return workShiftId;
    }

    public void setWorkShiftId(Long workShiftId) {
        this.workShiftId = workShiftId;
    }

    public Long getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Long userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Long getAssignedByUserAccountId() {
        return assignedByUserAccountId;
    }

    public void setAssignedByUserAccountId(Long assignedByUserAccountId) {
        this.assignedByUserAccountId = assignedByUserAccountId;
    }

    public Long getConfirmedByUserAccountId() {
        return confirmedByUserAccountId;
    }

    public void setConfirmedByUserAccountId(Long confirmedByUserAccountId) {
        this.confirmedByUserAccountId = confirmedByUserAccountId;
    }

    public Long getReleasedByUserAccountId() {
        return releasedByUserAccountId;
    }

    public void setReleasedByUserAccountId(Long releasedByUserAccountId) {
        this.releasedByUserAccountId = releasedByUserAccountId;
    }

    public ShiftAssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShiftAssignmentStatus status) {
        this.status = status;
    }

    public String getReleaseReason() {
        return releaseReason;
    }

    public void setReleaseReason(String releaseReason) {
        this.releaseReason = releaseReason;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Instant getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(Instant confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public Instant getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(Instant releasedAt) {
        this.releasedAt = releasedAt;
    }
}