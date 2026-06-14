package com.vitalwatch.center.platform.staffrecovery.application.internal.commandservices;

import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;
import com.vitalwatch.center.platform.staffrecovery.application.commandservices.RecoveryCommandService;
import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryAction;
import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryPlan;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.AddRecoveryActionCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CancelRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CompleteRecoveryActionCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CompleteRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CreateRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.StartRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.repositories.RecoveryActionRepository;
import com.vitalwatch.center.platform.staffrecovery.domain.repositories.RecoveryPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Staff recovery command service implementation.
 */
@Service
public class RecoveryCommandServiceImpl implements RecoveryCommandService {

    private final RecoveryPlanRepository recoveryPlanRepository;
    private final RecoveryActionRepository recoveryActionRepository;

    public RecoveryCommandServiceImpl(
            RecoveryPlanRepository recoveryPlanRepository,
            RecoveryActionRepository recoveryActionRepository
    ) {
        this.recoveryPlanRepository = recoveryPlanRepository;
        this.recoveryActionRepository = recoveryActionRepository;
    }

    @Override
    @Transactional
    public Result<RecoveryPlan, ApplicationError> handle(CreateRecoveryPlanCommand command) {
        try {
            var recoveryPlan = new RecoveryPlan(command);
            var savedRecoveryPlan = recoveryPlanRepository.save(recoveryPlan);

            return Result.success(savedRecoveryPlan);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("RecoveryPlan", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Recovery plan creation", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<RecoveryPlan, ApplicationError> handle(StartRecoveryPlanCommand command) {
        try {
            var recoveryPlan = recoveryPlanRepository.findById(command.recoveryPlanId());

            if (recoveryPlan.isEmpty()) {
                return Result.failure(ApplicationError.notFound("RecoveryPlan", command.recoveryPlanId().toString()));
            }

            var planToStart = recoveryPlan.get();
            planToStart.start(command);

            var savedRecoveryPlan = recoveryPlanRepository.save(planToStart);

            return Result.success(savedRecoveryPlan);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("RecoveryPlan", exception.getMessage()));
        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("RecoveryPlan", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Recovery plan start", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<RecoveryPlan, ApplicationError> handle(CompleteRecoveryPlanCommand command) {
        try {
            var recoveryPlan = recoveryPlanRepository.findById(command.recoveryPlanId());

            if (recoveryPlan.isEmpty()) {
                return Result.failure(ApplicationError.notFound("RecoveryPlan", command.recoveryPlanId().toString()));
            }

            var planToComplete = recoveryPlan.get();
            planToComplete.complete(command);

            var savedRecoveryPlan = recoveryPlanRepository.save(planToComplete);

            return Result.success(savedRecoveryPlan);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("RecoveryPlan", exception.getMessage()));
        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("RecoveryPlan", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Recovery plan completion", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<RecoveryPlan, ApplicationError> handle(CancelRecoveryPlanCommand command) {
        try {
            var recoveryPlan = recoveryPlanRepository.findById(command.recoveryPlanId());

            if (recoveryPlan.isEmpty()) {
                return Result.failure(ApplicationError.notFound("RecoveryPlan", command.recoveryPlanId().toString()));
            }

            var planToCancel = recoveryPlan.get();
            planToCancel.cancel(command);

            var savedRecoveryPlan = recoveryPlanRepository.save(planToCancel);

            return Result.success(savedRecoveryPlan);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("RecoveryPlan", exception.getMessage()));
        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("RecoveryPlan", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Recovery plan cancellation", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<RecoveryAction, ApplicationError> handle(AddRecoveryActionCommand command) {
        try {
            var recoveryPlan = recoveryPlanRepository.findById(command.recoveryPlanId());

            if (recoveryPlan.isEmpty()) {
                return Result.failure(ApplicationError.notFound("RecoveryPlan", command.recoveryPlanId().toString()));
            }

            var plan = recoveryPlan.get();

            if (plan.isCancelled()) {
                return Result.failure(ApplicationError.businessRuleViolation(
                        "RecoveryPlan",
                        "Cancelled recovery plans cannot receive actions"
                ));
            }

            if (plan.isCompleted()) {
                return Result.failure(ApplicationError.businessRuleViolation(
                        "RecoveryPlan",
                        "Completed recovery plans cannot receive actions"
                ));
            }

            var recoveryAction = new RecoveryAction(command);
            var savedRecoveryAction = recoveryActionRepository.save(recoveryAction);

            return Result.success(savedRecoveryAction);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("RecoveryAction", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Recovery action creation", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<RecoveryAction, ApplicationError> handle(CompleteRecoveryActionCommand command) {
        try {
            var recoveryAction = recoveryActionRepository.findById(command.recoveryActionId());

            if (recoveryAction.isEmpty()) {
                return Result.failure(ApplicationError.notFound("RecoveryAction", command.recoveryActionId().toString()));
            }

            var actionToComplete = recoveryAction.get();
            actionToComplete.complete(command);

            var savedRecoveryAction = recoveryActionRepository.save(actionToComplete);

            return Result.success(savedRecoveryAction);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("RecoveryAction", exception.getMessage()));
        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("RecoveryAction", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Recovery action completion", exception.getMessage()));
        }
    }
}