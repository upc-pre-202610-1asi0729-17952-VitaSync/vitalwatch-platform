package com.vitalwatch.center.platform.shifts.application.internal.queryservices;

import com.vitalwatch.center.platform.shifts.application.queryservices.ShiftQueryService;
import com.vitalwatch.center.platform.shifts.domain.model.aggregates.ShiftAssignment;
import com.vitalwatch.center.platform.shifts.domain.model.aggregates.WorkShift;
import com.vitalwatch.center.platform.shifts.domain.model.queries.GetShiftAssignmentsByUserAccountIdQuery;
import com.vitalwatch.center.platform.shifts.domain.model.queries.GetShiftAssignmentsByWorkShiftIdQuery;
import com.vitalwatch.center.platform.shifts.domain.model.queries.GetWorkShiftByIdQuery;
import com.vitalwatch.center.platform.shifts.domain.model.queries.GetWorkShiftsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.shifts.domain.repositories.ShiftAssignmentRepository;
import com.vitalwatch.center.platform.shifts.domain.repositories.WorkShiftRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Shift query service implementation.
 */
@Service
public class ShiftQueryServiceImpl implements ShiftQueryService {

    private final WorkShiftRepository workShiftRepository;
    private final ShiftAssignmentRepository shiftAssignmentRepository;

    public ShiftQueryServiceImpl(
            WorkShiftRepository workShiftRepository,
            ShiftAssignmentRepository shiftAssignmentRepository
    ) {
        this.workShiftRepository = workShiftRepository;
        this.shiftAssignmentRepository = shiftAssignmentRepository;
    }

    @Override
    public Optional<WorkShift> handle(GetWorkShiftByIdQuery query) {
        return workShiftRepository.findById(query.workShiftId());
    }

    @Override
    public List<WorkShift> handle(GetWorkShiftsByHospitalWorkspaceIdQuery query) {
        return workShiftRepository.findAllByHospitalWorkspaceId(query.hospitalWorkspaceId());
    }

    @Override
    public List<ShiftAssignment> handle(GetShiftAssignmentsByWorkShiftIdQuery query) {
        return shiftAssignmentRepository.findAllByWorkShiftId(query.workShiftId());
    }

    @Override
    public List<ShiftAssignment> handle(GetShiftAssignmentsByUserAccountIdQuery query) {
        return shiftAssignmentRepository.findAllByUserAccountId(query.userAccountId());
    }
}