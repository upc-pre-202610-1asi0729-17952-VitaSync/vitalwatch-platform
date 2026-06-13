package com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.RegisterVitalSignReadingCommand;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.enums.VitalSignSource;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.valueobjects.HeartRateBpm;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.valueobjects.HoursQuantity;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.valueobjects.SelfReportedFatigueLevel;
import com.vitalwatch.center.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate root that represents physiological and workload data used for fatigue monitoring.
 */
public class VitalSignReading extends AbstractDomainAggregateRoot<VitalSignReading> {

    private Long id;
    private Long hospitalWorkspaceId;
    private Long userAccountId;
    private HeartRateBpm heartRateBpm;
    private HoursQuantity sleepHoursLast24h;
    private HoursQuantity shiftHoursLast24h;
    private SelfReportedFatigueLevel selfReportedFatigueLevel;
    private VitalSignSource source;
    private Instant recordedAt;

    public VitalSignReading(
            Long id,
            Long hospitalWorkspaceId,
            Long userAccountId,
            HeartRateBpm heartRateBpm,
            HoursQuantity sleepHoursLast24h,
            HoursQuantity shiftHoursLast24h,
            SelfReportedFatigueLevel selfReportedFatigueLevel,
            VitalSignSource source,
            Instant recordedAt
    ) {
        this.id = id;
        this.hospitalWorkspaceId = validatePositiveId(hospitalWorkspaceId, "hospitalWorkspaceId");
        this.userAccountId = validatePositiveId(userAccountId, "userAccountId");
        this.heartRateBpm = Objects.requireNonNull(heartRateBpm, "heartRateBpm must not be null");
        this.sleepHoursLast24h = Objects.requireNonNull(sleepHoursLast24h, "sleepHoursLast24h must not be null");
        this.shiftHoursLast24h = Objects.requireNonNull(shiftHoursLast24h, "shiftHoursLast24h must not be null");
        this.selfReportedFatigueLevel = Objects.requireNonNull(selfReportedFatigueLevel, "selfReportedFatigueLevel must not be null");
        this.source = Objects.requireNonNull(source, "source must not be null");
        this.recordedAt = Objects.requireNonNull(recordedAt, "recordedAt must not be null");
    }

    public VitalSignReading(RegisterVitalSignReadingCommand command) {
        this(
                null,
                command.hospitalWorkspaceId(),
                command.userAccountId(),
                new HeartRateBpm(command.heartRateBpm()),
                new HoursQuantity(command.sleepHoursLast24h()),
                new HoursQuantity(command.shiftHoursLast24h()),
                new SelfReportedFatigueLevel(command.selfReportedFatigueLevel()),
                command.source(),
                Instant.now()
        );
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

    public HeartRateBpm getHeartRateBpmValue() {
        return heartRateBpm;
    }

    public Integer getHeartRateBpm() {
        return heartRateBpm.value();
    }

    public HoursQuantity getSleepHoursLast24hValue() {
        return sleepHoursLast24h;
    }

    public Double getSleepHoursLast24h() {
        return sleepHoursLast24h.value();
    }

    public HoursQuantity getShiftHoursLast24hValue() {
        return shiftHoursLast24h;
    }

    public Double getShiftHoursLast24h() {
        return shiftHoursLast24h.value();
    }

    public SelfReportedFatigueLevel getSelfReportedFatigueLevelValue() {
        return selfReportedFatigueLevel;
    }

    public Integer getSelfReportedFatigueLevel() {
        return selfReportedFatigueLevel.value();
    }

    public VitalSignSource getSource() {
        return source;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }
}