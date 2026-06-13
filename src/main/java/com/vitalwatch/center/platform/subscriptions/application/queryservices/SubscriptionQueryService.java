package com.vitalwatch.center.platform.subscriptions.application.queryservices;

import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.HospitalSubscription;
import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.SubscriptionPlan;
import com.vitalwatch.center.platform.subscriptions.domain.model.queries.GetAllSubscriptionPlansQuery;
import com.vitalwatch.center.platform.subscriptions.domain.model.queries.GetSubscriptionByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.subscriptions.domain.model.queries.GetSubscriptionByIdQuery;
import com.vitalwatch.center.platform.subscriptions.domain.model.queries.GetSubscriptionPlanByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for subscription queries.
 */
public interface SubscriptionQueryService {

    List<SubscriptionPlan> handle(GetAllSubscriptionPlansQuery query);

    Optional<SubscriptionPlan> handle(GetSubscriptionPlanByIdQuery query);

    Optional<HospitalSubscription> handle(GetSubscriptionByIdQuery query);

    Optional<HospitalSubscription> handle(GetSubscriptionByHospitalWorkspaceIdQuery query);
}