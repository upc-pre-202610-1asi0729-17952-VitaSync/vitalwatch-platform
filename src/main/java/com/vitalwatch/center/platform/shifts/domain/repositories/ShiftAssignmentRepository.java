package com.vitalwatch.center.platform.shifts.domain.repositories;

import com.vitalwatch.center.platform.shifts.domain.model.aggregates.ShiftAssignment;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for shift assignments.
 */
public interface ShiftAssignmentRepository {

    Optional<ShiftAssignment> findById(Long id);

    List<ShiftAssignment> findAllByWorkShiftId(Long workShiftId);

    List<ShiftAssignment> findAllByUserAccountId(Long userAccountId);

    boolean existsActiveAssignmentByUserAccountId(Long userAccountId);

    ShiftAssignment save(ShiftAssignment shiftAssignment);
}