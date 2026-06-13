package com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.enums.VitalSignSource;
import com.vitalwatch.center.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;

import java.time.Instant;

/**
 * JPA persistence entity for vital sign readings.
 */
@Entity
@Table(name = "vital_sign_readings")
public class VitalSignReadingPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "hospital_workspace_id", nullable = false)
    private Long hospitalWorkspaceId;

    @Column(name = "user_account_id", nullable = false)
    private Long userAccountId;

    @Column(name = "heart_rate_bpm", nullable = false)
    private Integer heartRateBpm;

    @Column(name = "sleep_hours_last_24h", nullable = false)
    private Double sleepHoursLast24h;

    @Column(name = "shift_hours_last_24h", nullable = false)
    private Double shiftHoursLast24h;

    @Column(name = "self_reported_fatigue_level", nullable = false)
    private Integer selfReportedFatigueLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VitalSignSource source;

    @Column(name = "recorded_at", nullable = false)
    private Instant recordedAt;

    public VitalSignReadingPersistenceEntity() {
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

    public Integer getHeartRateBpm() {
        return heartRateBpm;
    }

    public void setHeartRateBpm(Integer heartRateBpm) {
        this.heartRateBpm = heartRateBpm;
    }

    public Double getSleepHoursLast24h() {
        return sleepHoursLast24h;
    }

    public void setSleepHoursLast24h(Double sleepHoursLast24h) {
        this.sleepHoursLast24h = sleepHoursLast24h;
    }

    public Double getShiftHoursLast24h() {
        return shiftHoursLast24h;
    }

    public void setShiftHoursLast24h(Double shiftHoursLast24h) {
        this.shiftHoursLast24h = shiftHoursLast24h;
    }

    public Integer getSelfReportedFatigueLevel() {
        return selfReportedFatigueLevel;
    }

    public void setSelfReportedFatigueLevel(Integer selfReportedFatigueLevel) {
        this.selfReportedFatigueLevel = selfReportedFatigueLevel;
    }

    public VitalSignSource getSource() {
        return source;
    }

    public void setSource(VitalSignSource source) {
        this.source = source;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(Instant recordedAt) {
        this.recordedAt = recordedAt;
    }
}