package com.vitalwatch.center.platform.shifts.domain.repositories;

import com.vitalwatch.center.platform.shifts.domain.model.aggregates.WorkShift;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for work shifts.
 */
public interface WorkShiftRepository {

    Optional<WorkShift> findById(Long id);

    List<WorkShift> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId);

    WorkShift save(WorkShift workShift);
}