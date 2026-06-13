package com.vitalwatch.center.platform.clinicalrisk.domain.repositories;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.VitalSignReading;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for vital sign readings.
 */
public interface VitalSignReadingRepository {

    Optional<VitalSignReading> findById(Long id);

    List<VitalSignReading> findAllByUserAccountId(Long userAccountId);

    List<VitalSignReading> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId);

    VitalSignReading save(VitalSignReading vitalSignReading);
}