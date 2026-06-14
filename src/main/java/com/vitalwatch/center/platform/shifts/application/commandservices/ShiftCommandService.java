package com.vitalwatch.center.platform.shifts.application.commandservices;

import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;
import com.vitalwatch.center.platform.shifts.domain.model.aggregates.ShiftAssignment;
import com.vitalwatch.center.platform.shifts.domain.model.aggregates.WorkShift;
import com.vitalwatch.center.platform.shifts.domain.model.commands.AssignUserToShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.CancelWorkShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.CompleteWorkShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.ConfirmShiftAssignmentCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.CreateWorkShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.ReleaseShiftAssignmentCommand;

/**
 * Application service contract for shift commands.
 */
public interface ShiftCommandService {

    Result<WorkShift, ApplicationError> handle(CreateWorkShiftCommand command);

    Result<ShiftAssignment, ApplicationError> handle(AssignUserToShiftCommand command);

    Result<ShiftAssignment, ApplicationError> handle(ConfirmShiftAssignmentCommand command);

    Result<ShiftAssignment, ApplicationError> handle(ReleaseShiftAssignmentCommand command);

    Result<WorkShift, ApplicationError> handle(CancelWorkShiftCommand command);

    Result<WorkShift, ApplicationError> handle(CompleteWorkShiftCommand command);
}