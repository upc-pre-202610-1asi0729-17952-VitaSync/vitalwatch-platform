package com.vitalwatch.center.platform.shifts.application.queryservices;

import com.vitalwatch.center.platform.shifts.domain.model.aggregates.ShiftAssignment;
import com.vitalwatch.center.platform.shifts.domain.model.aggregates.WorkShift;
import com.vitalwatch.center.platform.shifts.domain.model.queries.GetShiftAssignmentsByUserAccountIdQuery;
import com.vitalwatch.center.platform.shifts.domain.model.queries.GetShiftAssignmentsByWorkShiftIdQuery;
import com.vitalwatch.center.platform.shifts.domain.model.queries.GetWorkShiftByIdQuery;
import com.vitalwatch.center.platform.shifts.domain.model.queries.GetWorkShiftsByHospitalWorkspaceIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for shift queries.
 */
public interface ShiftQueryService {

    Optional<WorkShift> handle(GetWorkShiftByIdQuery query);

    List<WorkShift> handle(GetWorkShiftsByHospitalWorkspaceIdQuery query);

    List<ShiftAssignment> handle(GetShiftAssignmentsByWorkShiftIdQuery query);

    List<ShiftAssignment> handle(GetShiftAssignmentsByUserAccountIdQuery query);
}