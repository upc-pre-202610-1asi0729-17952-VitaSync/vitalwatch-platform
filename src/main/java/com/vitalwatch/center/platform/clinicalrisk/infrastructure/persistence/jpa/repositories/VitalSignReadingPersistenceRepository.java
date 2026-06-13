package com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.entities.VitalSignReadingPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for vital sign readings.
 */
public interface VitalSignReadingPersistenceRepository extends JpaRepository<VitalSignReadingPersistenceEntity, Long> {

    List<VitalSignReadingPersistenceEntity> findAllByUserAccountIdOrderByRecordedAtDesc(Long userAccountId);

    List<VitalSignReadingPersistenceEntity> findAllByHospitalWorkspaceIdOrderByRecordedAtDesc(Long hospitalWorkspaceId);
}