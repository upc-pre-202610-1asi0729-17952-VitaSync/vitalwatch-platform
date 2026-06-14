package com.vitalwatch.center.platform.shifts.application.internal.commandservices;

import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;
import com.vitalwatch.center.platform.shifts.application.commandservices.ShiftCommandService;
import com.vitalwatch.center.platform.shifts.domain.model.aggregates.ShiftAssignment;
import com.vitalwatch.center.platform.shifts.domain.model.aggregates.WorkShift;
import com.vitalwatch.center.platform.shifts.domain.model.commands.AssignUserToShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.CancelWorkShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.CompleteWorkShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.ConfirmShiftAssignmentCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.CreateWorkShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.ReleaseShiftAssignmentCommand;
import com.vitalwatch.center.platform.shifts.domain.repositories.ShiftAssignmentRepository;
import com.vitalwatch.center.platform.shifts.domain.repositories.WorkShiftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Shift command service implementation.
 */
@Service
public class ShiftCommandServiceImpl implements ShiftCommandService {

    private final WorkShiftRepository workShiftRepository;
    private final ShiftAssignmentRepository shiftAssignmentRepository;

    public ShiftCommandServiceImpl(
            WorkShiftRepository workShiftRepository,
            ShiftAssignmentRepository shiftAssignmentRepository
    ) {
        this.workShiftRepository = workShiftRepository;
        this.shiftAssignmentRepository = shiftAssignmentRepository;
    }

    @Override
    @Transactional
    public Result<WorkShift, ApplicationError> handle(CreateWorkShiftCommand command) {
        try {
            var workShift = new WorkShift(command);
            var savedWorkShift = workShiftRepository.save(workShift);

            return Result.success(savedWorkShift);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("WorkShift", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Work shift creation", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<ShiftAssignment, ApplicationError> handle(AssignUserToShiftCommand command) {
        try {
            var workShift = workShiftRepository.findById(command.workShiftId());

            if (workShift.isEmpty()) {
                return Result.failure(ApplicationError.notFound("WorkShift", command.workShiftId().toString()));
            }

            var shift = workShift.get();

            if (shift.isCancelled()) {
                return Result.failure(ApplicationError.businessRuleViolation(
                        "WorkShift",
                        "Cancelled shifts cannot receive assignments"
                ));
            }

            if (shift.isCompleted()) {
                return Result.failure(ApplicationError.businessRuleViolation(
                        "WorkShift",
                        "Completed shifts cannot receive assignments"
                ));
            }

            var userAlreadyHasActiveAssignment = shiftAssignmentRepository.existsActiveAssignmentByUserAccountId(
                    command.userAccountId()
            );

            if (userAlreadyHasActiveAssignment) {
                return Result.failure(ApplicationError.businessRuleViolation(
                        "ShiftAssignment",
                        "User already has an active shift assignment"
                ));
            }

            var assignment = new ShiftAssignment(command);
            var savedAssignment = shiftAssignmentRepository.save(assignment);

            return Result.success(savedAssignment);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("ShiftAssignment", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Shift assignment creation", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<ShiftAssignment, ApplicationError> handle(ConfirmShiftAssignmentCommand command) {
        try {
            var assignment = shiftAssignmentRepository.findById(command.shiftAssignmentId());

            if (assignment.isEmpty()) {
                return Result.failure(ApplicationError.notFound(
                        "ShiftAssignment",
                        command.shiftAssignmentId().toString()
                ));
            }

            var assignmentToConfirm = assignment.get();
            assignmentToConfirm.confirm(command);

            var savedAssignment = shiftAssignmentRepository.save(assignmentToConfirm);

            return Result.success(savedAssignment);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("ShiftAssignment", exception.getMessage()));
        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("ShiftAssignment", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Shift assignment confirmation", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<ShiftAssignment, ApplicationError> handle(ReleaseShiftAssignmentCommand command) {
        try {
            var assignment = shiftAssignmentRepository.findById(command.shiftAssignmentId());

            if (assignment.isEmpty()) {
                return Result.failure(ApplicationError.notFound(
                        "ShiftAssignment",
                        command.shiftAssignmentId().toString()
                ));
            }

            var assignmentToRelease = assignment.get();
            assignmentToRelease.release(command);

            var savedAssignment = shiftAssignmentRepository.save(assignmentToRelease);

            return Result.success(savedAssignment);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("ShiftAssignment", exception.getMessage()));
        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("ShiftAssignment", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Shift assignment release", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<WorkShift, ApplicationError> handle(CancelWorkShiftCommand command) {
        try {
            var workShift = workShiftRepository.findById(command.workShiftId());

            if (workShift.isEmpty()) {
                return Result.failure(ApplicationError.notFound("WorkShift", command.workShiftId().toString()));
            }

            var shiftToCancel = workShift.get();
            shiftToCancel.cancel(command);

            var savedShift = workShiftRepository.save(shiftToCancel);

            return Result.success(savedShift);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("WorkShift", exception.getMessage()));
        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("WorkShift", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Work shift cancellation", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<WorkShift, ApplicationError> handle(CompleteWorkShiftCommand command) {
        try {
            var workShift = workShiftRepository.findById(command.workShiftId());

            if (workShift.isEmpty()) {
                return Result.failure(ApplicationError.notFound("WorkShift", command.workShiftId().toString()));
            }

            var shiftToComplete = workShift.get();
            shiftToComplete.complete(command);

            var savedShift = workShiftRepository.save(shiftToComplete);

            return Result.success(savedShift);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("WorkShift", exception.getMessage()));
        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("WorkShift", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Work shift completion", exception.getMessage()));
        }
    }
}