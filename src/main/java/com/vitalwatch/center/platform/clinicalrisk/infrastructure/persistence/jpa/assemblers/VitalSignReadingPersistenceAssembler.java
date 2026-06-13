package com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.assemblers;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.VitalSignReading;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.valueobjects.HeartRateBpm;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.valueobjects.HoursQuantity;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.valueobjects.SelfReportedFatigueLevel;
import com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.entities.VitalSignReadingPersistenceEntity;

/**
 * Assembler between VitalSignReading domain and persistence representations.
 */
public final class VitalSignReadingPersistenceAssembler {

    private VitalSignReadingPersistenceAssembler() {
    }

    public static VitalSignReading toDomainFromPersistence(VitalSignReadingPersistenceEntity entity) {
        return new VitalSignReading(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getUserAccountId(),
                new HeartRateBpm(entity.getHeartRateBpm()),
                new HoursQuantity(entity.getSleepHoursLast24h()),
                new HoursQuantity(entity.getShiftHoursLast24h()),
                new SelfReportedFatigueLevel(entity.getSelfReportedFatigueLevel()),
                entity.getSource(),
                entity.getRecordedAt()
        );
    }

    public static VitalSignReadingPersistenceEntity toPersistenceFromDomain(VitalSignReading aggregate) {
        var entity = new VitalSignReadingPersistenceEntity();
        entity.setId(aggregate.getId());
        entity.setHospitalWorkspaceId(aggregate.getHospitalWorkspaceId());
        entity.setUserAccountId(aggregate.getUserAccountId());
        entity.setHeartRateBpm(aggregate.getHeartRateBpm());
        entity.setSleepHoursLast24h(aggregate.getSleepHoursLast24h());
        entity.setShiftHoursLast24h(aggregate.getShiftHoursLast24h());
        entity.setSelfReportedFatigueLevel(aggregate.getSelfReportedFatigueLevel());
        entity.setSource(aggregate.getSource());
        entity.setRecordedAt(aggregate.getRecordedAt());
        return entity;
    }
}