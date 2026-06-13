package com.vitalwatch.center.platform.subscriptions.application.commandservices;

import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;
import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.HospitalSubscription;
import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.SubscriptionPlan;
import com.vitalwatch.center.platform.subscriptions.domain.model.commands.CancelSubscriptionCommand;
import com.vitalwatch.center.platform.subscriptions.domain.model.commands.ChangeSubscriptionPlanCommand;
import com.vitalwatch.center.platform.subscriptions.domain.model.commands.CreateSubscriptionPlanCommand;
import com.vitalwatch.center.platform.subscriptions.domain.model.commands.SubscribeHospitalWorkspaceCommand;

/**
 * Application service contract for subscription commands.
 */
public interface SubscriptionCommandService {

    Result<SubscriptionPlan, ApplicationError> handle(CreateSubscriptionPlanCommand command);

    Result<HospitalSubscription, ApplicationError> handle(SubscribeHospitalWorkspaceCommand command);

    Result<HospitalSubscription, ApplicationError> handle(ChangeSubscriptionPlanCommand command);

    Result<HospitalSubscription, ApplicationError> handle(CancelSubscriptionCommand command);
}