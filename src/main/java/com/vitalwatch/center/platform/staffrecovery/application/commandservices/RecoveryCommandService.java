package com.vitalwatch.center.platform.staffrecovery.application.commandservices;

import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;
import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryAction;
import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryPlan;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.AddRecoveryActionCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CancelRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CompleteRecoveryActionCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CompleteRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CreateRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.StartRecoveryPlanCommand;

/**
 * Application service contract for staff recovery commands.
 */
public interface RecoveryCommandService {

    Result<RecoveryPlan, ApplicationError> handle(CreateRecoveryPlanCommand command);

    Result<RecoveryPlan, ApplicationError> handle(StartRecoveryPlanCommand command);

    Result<RecoveryPlan, ApplicationError> handle(CompleteRecoveryPlanCommand command);

    Result<RecoveryPlan, ApplicationError> handle(CancelRecoveryPlanCommand command);

    Result<RecoveryAction, ApplicationError> handle(AddRecoveryActionCommand command);

    Result<RecoveryAction, ApplicationError> handle(CompleteRecoveryActionCommand command);
}